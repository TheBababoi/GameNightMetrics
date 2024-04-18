package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.entity.*;
import com.methodus.gamenightmetricsapp.service.BoardGameService;
import com.methodus.gamenightmetricsapp.service.GameRatingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/boardgames")
public class BoardGameController {
    private BoardGameService boardGameService;
    private BoardGameConfig boardGameConfig;
    private GameRatingsService gameRatingsService;
    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    public BoardGameController(BoardGameService boardGameService, BoardGameConfig boardGameConfig, GameRatingsService gameRatingsService) {
        this.boardGameService = boardGameService;
        this.boardGameConfig = boardGameConfig;
        this.gameRatingsService = gameRatingsService;
    }


    @GetMapping("/list")
    public String listOfBoardgames(Model model){
        //get the list of boardgames from th db
        List<BoardGame> boardGames = boardGameService.findAll();
        //add the list to the model
        model.addAttribute("boardgames",boardGames);
        return "boardgames/list-boardgames";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        model.addAttribute("boardgame",new DtoBoardGame());
        model.addAttribute("gameTypes",boardGameConfig.GAME_TYPES);
        model.addAttribute("playerNumbers",boardGameConfig.PLAYER_NUMBERS);

        return "boardgames/boardgame-form";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("boardgameId") int id, Model model){
        // get the boardgame from the service
        BoardGame boardGame = boardGameService.findById(id);
        //transfer the data to the dto
        DtoBoardGame dtoBoardGame = new DtoBoardGame();
        dtoBoardGame.setId(boardGame.getId());
        dtoBoardGame.setName(boardGame.getName());
        dtoBoardGame.setGameType(boardGame.getGameType());
        dtoBoardGame.setMaxPlayers(boardGame.getMaxPlayers());
        dtoBoardGame.setMinPlayers(boardGame.getMinPlayers());
        //set boardgame in the model to precalculate the form
        model.addAttribute("boardgame",dtoBoardGame);
        model.addAttribute("gameTypes",boardGameConfig.GAME_TYPES);
        model.addAttribute("playerNumbers",boardGameConfig.PLAYER_NUMBERS);

        // Pre-selected game types (split into a list)
        List<String> preselectedGameTypes = Collections.singletonList(boardGame.getGameType());
        if (preselectedGameTypes.get(0) != null) {
            // Safe to split here
            preselectedGameTypes = Arrays.asList(preselectedGameTypes.get(0).split(","));
        } else {
            preselectedGameTypes = new ArrayList<>();  // Initialize empty list if null
        }

        // Print the preselectedGameTypes list for verification
        System.out.println("Preselected Game Types: " + preselectedGameTypes);

        model.addAttribute("preselectedGameTypes", preselectedGameTypes);


        //send over the form
        return "boardgames/boardgame-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("boardgameId") int id){
        // delete the boardgame
        boardGameService.deleteById(id);
        //redirect to the /boardgames/list
        return "redirect:/boardgames/list";
    }
    @PostMapping("/save")
    public String saveBoardGame(@Valid @ModelAttribute("boardgame") DtoBoardGame dtoBoardGame, BindingResult bindingResult,
                                Model model) {
        logger.info("Processing Player form for: " + dtoBoardGame.getName());

        // form validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("gameTypes", boardGameConfig.GAME_TYPES);
            model.addAttribute("playerNumbers", boardGameConfig.PLAYER_NUMBERS);
            return "boardgames/boardgame-form";
        }

        //check if the minimum players are >= max players
        if (dtoBoardGame.getMinPlayers()>dtoBoardGame.getMaxPlayers()){
            model.addAttribute("boardgame", dtoBoardGame);
            model.addAttribute("gameTypes", boardGameConfig.GAME_TYPES);
            model.addAttribute("playerNumbers", boardGameConfig.PLAYER_NUMBERS);
            model.addAttribute("formError", "Minimum players are more than the maximum.");

            logger.warning("Minimum players are more than the maximum.");
            return "boardgames/boardgame-form";
        }

        // check the database if name already exists
        BoardGame boardGame = boardGameService.findByBoardGameName(dtoBoardGame.getName());

        if ((boardGame != null) && (boardGame.getId() != dtoBoardGame.getId())) {
            model.addAttribute("boardgame", dtoBoardGame);
            model.addAttribute("gameTypes", boardGameConfig.GAME_TYPES);
            model.addAttribute("playerNumbers", boardGameConfig.PLAYER_NUMBERS);
            model.addAttribute("formError", "Board Game name already exists.");

            logger.warning("Board game name already exists.");
            return "boardgames/boardgame-form";
        }

        //save the boardgame
        boardGameService.save(dtoBoardGame);
            return "redirect:/boardgames/list";
    }

    @GetMapping("/showFormForRating")
    public String showFormForRating(@RequestParam("boardgameId") int id, Model model) {
        BoardGame boardGame = boardGameService.findById(id);
        model.addAttribute("boardgame",boardGame);
        return "boardgames/boardgame-rating";

    }

    @PostMapping("/saveRating")
    public String saveRating(@RequestParam("boardgameId") int boardgameId,
                             @RequestParam("totalRating") int totalRating,
                             @RequestParam("gameplayRating") int gameplayrating,
                             @RequestParam("themeRating") int themeRating,
                             @RequestParam("visualRating") int visualRating,
                             @RequestParam("difficultyRating") int difficultyRating,
                             @RequestParam(value = "comment", required = false) String comment,
                             HttpServletRequest request ,
                             Model model) {

        BoardGame boardGame = boardGameService.findById(boardgameId);
        Player player = (Player) request.getSession().getAttribute("player");

        GameRatingsPK gameRatingsPK = new GameRatingsPK(player.getId(),boardgameId);
        GameRatings gameRatings = new GameRatings(gameRatingsPK,totalRating,gameplayrating,themeRating,visualRating,difficultyRating,comment);
        gameRatingsService.save(gameRatings);


        return "boardgames/rating-success";
    }

    @GetMapping("/showBoardgameRatings")
    public String showBoardgameRatings(@RequestParam("boardgameId") int boardgameId, Model model) {
        List<GameRatings> gameRatingsList = gameRatingsService.getGameRatingsForBoardgame(boardgameId);
        BoardGame boardGame = boardGameService.findById(boardgameId);
        model.addAttribute("boardgame",boardGame);
        model.addAttribute("gameRatings",gameRatingsList);
        System.out.println(calculateAverageRatings(gameRatingsList));
        model.addAttribute("averages",calculateAverageRatings(gameRatingsList));
        model.addAttribute("numberOfRatings",gameRatingsList.size());
        return "boardgames/ratings-display";
    }




    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    public static List<Double> calculateAverageRatings(List<GameRatings> gameRatingsList) {
        List<Double> averageRatings = new ArrayList<>();
        if (gameRatingsList == null || gameRatingsList.isEmpty()) {
            for (int i = 0; i <5 ; i++) {
                averageRatings.add(0.0);
            }
            System.out.println(averageRatings.get(0));
            return averageRatings;
        }

        int totalRatings = gameRatingsList.size();
        double totalOverallRating = 0.0, totalGameplayRating = 0.0, totalThemeRating = 0.0;
        double totalVisualsRating = 0.0, totalDifficultyRating = 0.0;

        for (GameRatings rating : gameRatingsList) {
            totalOverallRating += rating.getTotalRating();
            totalGameplayRating += rating.getGameplayRating();
            totalThemeRating += rating.getThemeRating();
            totalVisualsRating += rating.getVisualRating();
            totalDifficultyRating += rating.getDifficultyRating();
        }


        averageRatings.add(totalOverallRating / totalRatings);
        averageRatings.add(totalGameplayRating / totalRatings);
        averageRatings.add(totalThemeRating / totalRatings);
        averageRatings.add(totalVisualsRating / totalRatings);
        averageRatings.add(totalDifficultyRating / totalRatings);

        return averageRatings;
    }



}
