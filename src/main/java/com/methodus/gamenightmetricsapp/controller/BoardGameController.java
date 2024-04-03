package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.BoardGameConfig;
import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.DtoBoardGame;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.service.BoardGameService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private Logger logger = Logger.getLogger(getClass().getName());

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
            return "noardgames/boardgame-form";
        }

        //check if the minimum players are >= max players
        if (dtoBoardGame.getMinPlayers()>=dtoBoardGame.getMaxPlayers()){
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




    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

}
