package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.service.BoardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/boardgames")
public class BoardGameController {
    private BoardGameService boardGameService;
    private BoardGameConfig boardGameConfig;

    @Autowired
    public BoardGameController(BoardGameService boardGameService, BoardGameConfig boardGameConfig) {
        this.boardGameService = boardGameService;
        this.boardGameConfig = boardGameConfig;
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

        BoardGame boardGame = new BoardGame();
        // add to the spring model
        model.addAttribute("boardgame",boardGame);
        model.addAttribute("gameTypes",boardGameConfig.GAME_TYPES);
        model.addAttribute("playerNumbers",boardGameConfig.PLAYER_NUMBERS);

        return "boardgames/boardgame-form";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("boardgameId") int id, Model model){
        // get the boardgame from the service
        BoardGame boardGame = boardGameService.findById(id);
        //set boardgame in the model to precalculate the form
        model.addAttribute("boardgame",boardGame);
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
    public String saveBoardGame(@ModelAttribute("boardgame") BoardGame boardGame){
        //save the boardgame
        boardGameService.save(boardGame);

        //redirect to prevent duplicate submissions
        return "redirect:/boardgames/list";
    }
}
