package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.config.PlayerConfig;
import com.methodus.gamenightmetricsapp.entity.*;
import com.methodus.gamenightmetricsapp.service.BoardGameService;
import com.methodus.gamenightmetricsapp.service.PlayerGameStatsService;
import com.methodus.gamenightmetricsapp.service.PlayerService;
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
    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    public GameSessionController(PlayerService playerService, PlayerConfig playerConfig, BoardGameService boardGameService, BoardGameConfig boardGameConfig, PlayerGameStatsService playerGameStatsService) {
        this.playerService = playerService;
        this.playerConfig = playerConfig;
        this.boardGameService = boardGameService;
        this.boardGameConfig = boardGameConfig;
        this.playerGameStatsService = playerGameStatsService;
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
      model.addAttribute("players",players);
      model.addAttribute("boardgameId",boardgameId);
      return "gameSessions/player-winner";
    }


    @PostMapping("/saveSession")
    public String saveSession(@RequestParam("boardgameId") int boardgameId,
                              @RequestParam("players") List<Integer> players,
                              @RequestParam(name = "winners", required = false) List<Integer> winners) {

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

        List<PlayerGameStats> playerGameStatsList = new ArrayList<>();
        for (Integer playerId : players) {
            Player player = playerService.findById(playerId);
            DtoPlayer dtoPlayer = new DtoPlayer();
            dtoPlayer.copyFromPlayer(player);
            dtoPlayer.setTotalGamesPlayed(dtoPlayer.getTotalGamesPlayed()+1);
            System.out.println(dtoPlayer);
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
                playerGameStats.setWinLossRatio((double) (playerGameStats.getWins()/playerGameStats.getPlays())*100);
            }

            playerGameStatsList.add(playerGameStats);
        }

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
    public String getPlayerBoardGameStats(Model model) {
        List<Object[]> resultList = playerGameStatsService.getLeaderboardStats();
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







