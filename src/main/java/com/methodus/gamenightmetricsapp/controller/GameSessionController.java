package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.config.PlayerConfig;
import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;
import com.methodus.gamenightmetricsapp.service.BoardGameService;
import com.methodus.gamenightmetricsapp.service.PlayerGameStatsService;
import com.methodus.gamenightmetricsapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        System.out.println(possibleNumberOfPlayers);
        model.addAttribute("boardgame", boardGame);
        model.addAttribute("possibleNumberOfPlayers",possibleNumberOfPlayers);
        return "gameSessions/player-numbers";
    }

    @PostMapping("/selectPlayers")
    public String selectPlayers(@RequestParam("boardgameId") int boardgameId,
                                @RequestParam("selectedPlayerNumber") int selectedPlayerNumber,
                                Model model) {
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
                playerGameStats.setWinLossRatio(0.00);
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
                playerGameStats.setWinLossRatio((double) playerGameStats.getWins()/playerGameStats.getPlays());
            }

            playerGameStatsList.add(playerGameStats);
        }

        playerGameStatsService.saveAll(playerGameStatsList);

        return "gameSessions/gamesession-success";
    }





}
