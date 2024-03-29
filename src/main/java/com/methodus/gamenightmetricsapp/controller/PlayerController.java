package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.PlayerConfig;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/players")
public class PlayerController {
    private PlayerService playerService;

    private PlayerConfig playerConfig;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerConfig playerConfig) {
        this.playerService = playerService;
        this.playerConfig = playerConfig;
    }
    @GetMapping("/list")
    public String listOfPlayers(Model model){
        //get the list of players from th db
        List<Player> players = playerService.findAll();
        //add the list to the model
        model.addAttribute("players",players);
        return "players/list-players";
    }
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        Player player = new Player();
        // add to the spring model
        model.addAttribute(player);
        model.addAttribute("skillLevels", playerConfig.SKILL_LEVELS);
        model.addAttribute("preferredGameTypes", playerConfig.GAME_TYPES);
        model.addAttribute("playStyles", playerConfig.PLAYER_TYPES);

        return "players/player-form";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("playerId") int id, Model model){
        // get the player from the service
        Player player = playerService.findById(id);
        //set player in the model to precalculate the form
        model.addAttribute("player",player);
        model.addAttribute("skillLevels", playerConfig.SKILL_LEVELS);
        model.addAttribute("preferredGameTypes", playerConfig.GAME_TYPES);
        model.addAttribute("playStyles", playerConfig.PLAYER_TYPES);


        // Pre-selected game types (split into a list)
        List<String> preselectedGameTypes = Collections.singletonList(player.getPreferredGameType());
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
        return "players/player-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("playerId") int id){
        // delete the player
        playerService.deleteById(id);
        //redirect to the /players/list
        return "redirect:/players/list";
    }
    @PostMapping("/save")
    public String savePlayer(@ModelAttribute("player") Player player){
        //save the player
        playerService.save(player);

        //redirect to prevent duplicate submissions
        return "redirect:/players/list";
    }
}




