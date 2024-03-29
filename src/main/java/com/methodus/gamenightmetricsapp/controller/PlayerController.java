package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/players")
public class PlayerController {
    private PlayerService playerService;
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
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

        return "players/player-form";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("playerId") int id, Model model){
        // get the player from the service
        Player player = playerService.findById(id);
        //set player in the model to precalculate the form
        model.addAttribute("player",player);
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




