package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.config.PlayerConfig;
import com.methodus.gamenightmetricsapp.entity.*;
import com.methodus.gamenightmetricsapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

@Controller
@RequestMapping("/gamesessions")
public class GameSessionController {
    private PlayerService playerService;

    private PlayerConfig playerConfig;
    private BoardGameService boardGameService;
    private BoardGameConfig boardGameConfig;
    private PlayerGameStatsService playerGameStatsService;
    private GameSessionService gameSessionService;
    private ParticipatingPlayerService participatingPlayerService;
    private SessionWinnerService sessionWinnerService;
    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    public GameSessionController(PlayerService playerService, PlayerConfig playerConfig, BoardGameService boardGameService, BoardGameConfig boardGameConfig, PlayerGameStatsService playerGameStatsService, GameSessionService gameSessionService, ParticipatingPlayerService participatingPlayerService, SessionWinnerService sessionWinnerService) {
        this.playerService = playerService;
        this.playerConfig = playerConfig;
        this.boardGameService = boardGameService;
        this.boardGameConfig = boardGameConfig;
        this.playerGameStatsService = playerGameStatsService;
        this.gameSessionService = gameSessionService;
        this.participatingPlayerService = participatingPlayerService;
        this.sessionWinnerService = sessionWinnerService;
    }



    @GetMapping("/showGameList")
    public String showGamelist(Model model){
        //get the list of boardgames from th db
        List<BoardGame> boardGames = boardGameService.findAll();
        //add the list to the model
        model.addAttribute("boardgames",boardGames);

        return "gameSessions/list-games";
    }

    @GetMapping("/showPlayerNumbers")
    public String showPlayerNumbers(@RequestParam("boardgameId") int id, Model model) {
        BoardGame boardGame = boardGameService.findById(id);
        List<Integer> possibleNumberOfPlayers = new ArrayList<>();
        for (int i = boardGame.getMinPlayers(); i <=boardGame.getMaxPlayers() ; i++) {
            possibleNumberOfPlayers.add(i);
        }
        model.addAttribute("boardgame", boardGame);
        model.addAttribute("possibleNumberOfPlayers",possibleNumberOfPlayers);
        return "gameSessions/player-numbers";
    }

    @PostMapping("/selectPlayers")
    public String selectPlayers(@RequestParam("boardgameId") int boardgameId,
                                @RequestParam("selectedPlayerNumber") int selectedPlayerNumber,
                                Model model) {
        //Validation
        if (selectedPlayerNumber > playerService.count()){
            BoardGame boardGame = boardGameService.findById(boardgameId);
            List<Integer> possibleNumberOfPlayers = new ArrayList<>();
            for (int i = boardGame.getMinPlayers(); i <=boardGame.getMaxPlayers() ; i++) {
                possibleNumberOfPlayers.add(i);
            }
            model.addAttribute("boardgame", boardGame);
            model.addAttribute("possibleNumberOfPlayers",possibleNumberOfPlayers);
            model.addAttribute("errorMessage", "Error: Not enough players in the Database");
            return "gameSessions/player-numbers";
        }
        List<Player> players = playerService.findAll();
        model.addAttribute("selectedPlayerNumber", selectedPlayerNumber);
        model.addAttribute("players", players);
        model.addAttribute("boardgameId", boardgameId);

        return "gameSessions/player-names";
    }

    @PostMapping("/selectWinners")
    public String selectWinners(@RequestParam("boardgameId") int boardgameId,
                                @RequestParam("selectedPlayers") List<Integer> selectedPlayers, Model model) {
        ArrayList<Player> players = new ArrayList<>();
        //Validation
        if (hasDuplicates(selectedPlayers)){
            List<Player> playerList = playerService.findAll();
            model.addAttribute("selectedPlayerNumber",selectedPlayers.size());
            model.addAttribute("players", playerList);
            model.addAttribute("boardgameId", boardgameId);
            model.addAttribute("errorMessage", "Error: Please choose only unique players");
            return "gameSessions/player-names";
        }
        for (Integer id : selectedPlayers){
            players.add(playerService.findById(id));
        }
        System.out.println(players);
        model.addAttribute("durations",boardGameConfig.GAME_TIME);
      model.addAttribute("players",players);
      model.addAttribute("boardgameId",boardgameId);
      return "gameSessions/player-winner";
    }


    @PostMapping("/saveSession")
    public String saveSession(@RequestParam("boardgameId") int boardgameId,
                              @RequestParam("players") List<Integer> players,
                              @RequestParam("sessionDuration") String duration,
                              @RequestParam(name = "winners", required = false) List<Integer> winners,Model model)
                              {
        if(winners==null){
            model.addAttribute("durations",boardGameConfig.GAME_TIME);
            List<Player> playerList = new ArrayList<>();
            for (Integer player :players){
                playerList.add(playerService.findById(player));
            }
            model.addAttribute("players",playerList);
            model.addAttribute("boardgameId",boardgameId);
            model.addAttribute("errorMessage", "Error: Please choose at least one winner");
            return "gameSessions/player-winner";
        }
        Optional<BoardGame> boardGame = Optional.ofNullable(boardGameService.findById(boardgameId));
        if (boardGame.isEmpty()){
            throw new RuntimeException("Board Game not found");
        }else {
            BoardGame tempBoardGame = boardGame.get();
            DtoBoardGame dtoBoardGame = new DtoBoardGame();
            dtoBoardGame.copyFromBoardGame(tempBoardGame);
            dtoBoardGame.setTotalGamesPlayed(dtoBoardGame.getTotalGamesPlayed()+1);
            System.out.println(dtoBoardGame);

            boardGameService.save(dtoBoardGame);
        }

        Map<Integer, PlayerGameStats> StatsMap = new HashMap<>();
        for (Integer playerId : players) {
            PlayerGameStatsPK pk = new PlayerGameStatsPK(playerId, boardgameId);
            PlayerGameStats existingStats = playerGameStatsService.findById(pk)
                    .orElse(null);
            StatsMap.put(playerId, existingStats);
        }
        GameSession gameSession = new GameSession();
        gameSession.setGameId(boardgameId);
        gameSession.setSessionDate(new Date());
        gameSession.setSessionDuration(duration);
        gameSession.setTotalPlayers(players.size());
        gameSessionService.save(gameSession);

        List<PlayerGameStats> playerGameStatsList = new ArrayList<>();
        List<ParticipatingPlayer> participatingPlayers = new ArrayList<>();
        for (Integer playerId : players) {
            Player player = playerService.findById(playerId);
            DtoPlayer dtoPlayer = new DtoPlayer();
            dtoPlayer.copyFromPlayer(player);
            dtoPlayer.setTotalGamesPlayed(dtoPlayer.getTotalGamesPlayed()+1);
            System.out.println(dtoPlayer);

            ParticipatingPlayer participatingPlayer = new ParticipatingPlayer();
            participatingPlayer.setId(new GameSessionPlayerPk(playerId,gameSession.getId()));
            participatingPlayer.setPlayer(playerService.findById(playerId));
            participatingPlayer.setGameSession(gameSession);
            participatingPlayers.add(participatingPlayer);

            playerService.save(dtoPlayer);

            PlayerGameStats playerGameStats;
            if (StatsMap.containsKey(playerId)&&StatsMap.get(playerId)!=null) {
                playerGameStats = StatsMap.get(playerId);
            } else {
                playerGameStats = new PlayerGameStats();
                playerGameStats.setBoardGame(boardGame.get());
                PlayerGameStatsPK pk = new PlayerGameStatsPK(playerId,boardgameId);
                playerGameStats.setId(pk);
                playerGameStats.setPlays(0);
                playerGameStats.setWins(0);
                playerGameStats.setLoses(0);
                playerGameStats.setWinLossRatio(00.00);
            }

            if (winners != null && winners.contains(playerId)) {

                playerGameStats.setWins(playerGameStats.getWins() + 1);
            } else {
                playerGameStats.setLoses(playerGameStats.getLoses() + 1);
            }

            playerGameStats.setPlays(playerGameStats.getPlays() + 1);
            if (playerGameStats.getLoses()==0){
                playerGameStats.setWinLossRatio(100.00);
            }else {
                playerGameStats.setWinLossRatio((double) 100 * playerGameStats.getWins() /playerGameStats.getPlays());

            }

            playerGameStatsList.add(playerGameStats);
        }

        List<SessionWinner> sessionWinnersList = new ArrayList<>();
            if (winners != null) {
                for (Integer winnerId : winners) {
                    SessionWinner winner = new SessionWinner();
                    winner.setGameSession(gameSession);
                    winner.setPlayer(playerService.findById(winnerId));
                    winner.setId(new GameSessionPlayerPk(winnerId, gameSession.getId()));
                    sessionWinnersList.add(winner);
                }

            }
        participatingPlayerService.saveAll(participatingPlayers);
        sessionWinnerService.saveAll(sessionWinnersList);
        playerGameStatsService.saveAll(playerGameStatsList);

        return "gameSessions/gamesession-success";
    }

    @GetMapping("/boardgamestats")
    public String getBoardGameStats(@RequestParam("boardgameId") int boardgameId, Model model) {
        List<PlayerGameStats> playerGameStatsList = playerGameStatsService.getPlayerStatsForBoardGame(boardgameId);
        BoardGame boardGame = boardGameService.findById(boardgameId);
        model.addAttribute("playerStats", playerGameStatsList);
        model.addAttribute("boardgame", boardGame);
        return "gameSessions/boardgame-display";
    }

    @GetMapping("/playerstats")
    public String getPlayerStats(@RequestParam("playerId") int id, Model model) {
        List<PlayerGameStats> playerGameStatsList = playerGameStatsService.getPlayerStatsForPlayer(id);
        System.out.println(playerGameStatsList);
        Player player = playerService.findById(id);
        model.addAttribute("playerStats", playerGameStatsList);
        model.addAttribute("player", player);
        return "gameSessions/player-display";
    }

    @GetMapping("/leaderboard")
    public String getPlayerBoardGameStatsGet(@RequestParam(required = false) String selectedGameType,Model model) {
        return getPlayerBoardGameStats(selectedGameType, model);
    }

    @PostMapping("/leaderboardpost")
    public String getPlayerBoardGameStatsPost(@RequestParam(required = false) String selectedGameType,Model model) {
        return getPlayerBoardGameStats(selectedGameType, model);
    }

    @GetMapping("/gameSessions")
    public String showGameSessions(@RequestParam(required = false) String boardGameId,Model model) {
        int gameId;
        if (boardGameId==null||boardGameId.isEmpty()){
            gameId = 0;
        }else {
            gameId = Integer.parseInt(boardGameId);
        }
        List<GameSession> gameSessions;
        if (gameId!=0) {
            gameSessions = gameSessionService.getGameSessions(gameId);
        } else {
            gameSessions = gameSessionService.getGameSessions(0);
        }

        Map<Integer, String> winners = new HashMap<>();
        Map<Integer,String> losers = new HashMap<>();
        List<BoardGame>boardGames = boardGameService.findAll();
        for (GameSession gameSession : gameSessions) {
            losers.put(gameSession.getId(), getLosers(gameSession.getId()));
            winners.put(gameSession.getId(), getWinners(gameSession.getId()));
        }
        model.addAttribute("winners",winners);
        model.addAttribute("losers",losers);
        model.addAttribute("gameSessions", gameSessions);
        model.addAttribute("boardgames",boardGames);
        return "gameSessions/display-gameSessions";
    }

    @PostMapping("/gamesessionspost")
    public String showGameSessionsPost(@RequestParam(required = false) String boardGameId,Model model) {
        int gameId;
        if (boardGameId==null||boardGameId.isEmpty()){
            gameId = 0;
        }else {
            gameId = Integer.parseInt(boardGameId);
        }
        List<GameSession> gameSessions;
        if (gameId!=0) {
            gameSessions = gameSessionService.getGameSessions(gameId);
        } else {
            gameSessions = gameSessionService.getGameSessions(0);
        }

        Map<Integer, String> winners = new HashMap<>();
        Map<Integer,String> losers = new HashMap<>();
        List<BoardGame>boardGames = boardGameService.findAll();
        for (GameSession gameSession : gameSessions) {
            losers.put(gameSession.getId(), getLosers(gameSession.getId()));
            winners.put(gameSession.getId(), getWinners(gameSession.getId()));
        }
        model.addAttribute("winners",winners);
        model.addAttribute("losers",losers);
        model.addAttribute("gameSessions", gameSessions);
        model.addAttribute("boardgames",boardGames);
        return "gameSessions/display-gameSessions";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("gameSessionId") int id){

        GameSession gameSession = gameSessionService.findById(id);
        //reducing the board game total plays
        BoardGame boardGame = boardGameService.findById(gameSession.getGameId());
        DtoBoardGame dtoBoardGame = new DtoBoardGame();
        dtoBoardGame.copyFromBoardGame(boardGame);
        dtoBoardGame.setTotalGamesPlayed(dtoBoardGame.getTotalGamesPlayed()-1);
        boardGameService.save(dtoBoardGame);

        List<SessionWinner> sessionWinners = sessionWinnerService.findByGameSession_Id(id);
        List<Integer> winnerIds = new ArrayList<>();
        for (SessionWinner sessionWinner : sessionWinners ){
            winnerIds.add(sessionWinner.getPlayer().getId());
        }
        List<ParticipatingPlayer> allPlayers = participatingPlayerService.findByGameSession_Id(id);
        for (ParticipatingPlayer participatingPlayer: allPlayers){
            PlayerGameStats playerGameStats = playerGameStatsService.findById(new PlayerGameStatsPK(participatingPlayer.getPlayer().getId(),gameSession.getGameId())).orElse(null);
            //reducing the player's total plays
            Player player = playerService.findById(participatingPlayer.getPlayer().getId());
            DtoPlayer dtoPlayer = new DtoPlayer();
            dtoPlayer.copyFromPlayer(player);
            dtoPlayer.setTotalGamesPlayed(dtoPlayer.getTotalGamesPlayed()-1);
            playerService.save(dtoPlayer);


            if (winnerIds.contains(participatingPlayer.getPlayer().getId())){
                System.out.println("removing win fromm    " + participatingPlayer.getPlayer());
                assert playerGameStats != null;
                playerGameStats.setWins(playerGameStats.getWins()-1);
            }else {
                assert playerGameStats != null;
                System.out.println("removing loss fromm    " + participatingPlayer.getPlayer());
                playerGameStats.setLoses(playerGameStats.getLoses()-1);
            }
            System.out.println("removing play fromm    " + participatingPlayer.getPlayer());
            playerGameStats.setPlays(playerGameStats.getPlays()-1);
            if (playerGameStats.getPlays()==0){
                playerGameStats.setWinLossRatio(0.00);
            } else if (playerGameStats.getLoses()==0){
                playerGameStats.setWinLossRatio(100.00);
            }else {
                playerGameStats.setWinLossRatio((double) 100 * playerGameStats.getWins() /playerGameStats.getPlays());

            }

            playerGameStatsService.save(playerGameStats);

        }
        gameSessionService.delete(id);
        return "/gameSessions/gamesession-deletion-success";
    }

    public String getWinners(int gameSessionId) {
        StringBuilder winnerNames = new StringBuilder();
        List<SessionWinner> sessionWinners = sessionWinnerService.findByGameSession_Id(gameSessionId);

        if (sessionWinners.isEmpty()) {
            return "";
        }

        for (int i = 0; i < sessionWinners.size() - 1; i++) {
            winnerNames.append(sessionWinners.get(i).getPlayer().getUsername()).append(", ");
        }
        winnerNames.append(sessionWinners.get(sessionWinners.size() - 1).getPlayer().getUsername());

        return winnerNames.toString();
    }

    public String getLosers(int gameSessionId) {
        List<SessionWinner> sessionWinners = sessionWinnerService.findByGameSession_Id(gameSessionId);
        List<ParticipatingPlayer> allPlayers = participatingPlayerService.findByGameSession_Id(gameSessionId);


        if (sessionWinners.isEmpty() || allPlayers.isEmpty()) {
            return "";
        }

        Set<String> winnerUsernames = new HashSet<>();
        for (SessionWinner winner : sessionWinners) {
            winnerUsernames.add(winner.getPlayer().getUsername());
        }

        StringBuilder loserNames = new StringBuilder();
        for (ParticipatingPlayer player : allPlayers) {
            if (!winnerUsernames.contains(player.getPlayer().getUsername())) {
                loserNames.append(player.getPlayer().getUsername()).append(", ");
            }
        }

        // Remove trailing comma and space (if any)
        if (loserNames.length() > 2) {
            loserNames.setLength(loserNames.length() - 2);
        }

        return loserNames.toString();
    }



    private String getPlayerBoardGameStats(@RequestParam(required = false) String selectedGameType, Model model) {
        List<Object[]> resultList;
        if (selectedGameType != null && !selectedGameType.isEmpty()) {
            resultList = playerGameStatsService.getLeaderboardStats(selectedGameType);
        } else {
            selectedGameType = null;
            resultList = playerGameStatsService.getLeaderboardStats(null);
        }
        List<Map<String, Object>> data = new ArrayList<>();

        for (Object[] result : resultList) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("player", result[0]);
            stats.put("wins", result[1]);
            stats.put("loses", result[2]);
            stats.put("plays", result[3]);
            if ((long)result[3] != 0) {
                double ratio = ((double) (long) result[1] / (long) result[3]) * 100;
                stats.put("ratio", ratio);
            } else {
                stats.put("ratio", 00.0);
            }
            data.add(stats);
        }
        model.addAttribute("selectedGameType",selectedGameType);
        model.addAttribute("gameTypes",boardGameConfig.GAME_TYPES);
        model.addAttribute("data",data);
        return "gameSessions/leaderboard-display";
    }

    public static boolean hasDuplicates(List<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        for (Integer element : list) {
            if (seen.contains(element)) {
                return true;
            }
            seen.add(element);
        }
        return false;
    }



}







