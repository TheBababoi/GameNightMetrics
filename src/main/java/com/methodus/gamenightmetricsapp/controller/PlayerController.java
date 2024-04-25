package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.config.PlayerConfig;
import com.methodus.gamenightmetricsapp.entity.DtoPlayer;
import com.methodus.gamenightmetricsapp.entity.GameRatings;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.service.GameRatingsService;
import com.methodus.gamenightmetricsapp.service.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


@Controller
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    private final PlayerConfig playerConfig;
    private final GameRatingsService gameRatingsService;
    private final Logger logger = Logger.getLogger(getClass().getName());


    @Autowired
    public PlayerController(PlayerService playerService, PlayerConfig playerConfig, GameRatingsService gameRatingsService) {
        this.playerService = playerService;
        this.playerConfig = playerConfig;
        this.gameRatingsService = gameRatingsService;
    }


    @GetMapping("/list")
    public String listOfPlayers(Model model) {
        //get the list of players from th db
        List<Player> players = playerService.findAll();
        //add the list to the model
        model.addAttribute("players", players);
        return "players/list-players";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        model.addAttribute("player", new DtoPlayer());
        model.addAttribute("skillLevels", playerConfig.SKILL_LEVELS);
        model.addAttribute("preferredGameTypes", playerConfig.GAME_TYPES);
        model.addAttribute("playStyles", playerConfig.PLAYER_TYPES);

        return "players/player-form";

    }

    @GetMapping("/showFormForRegister")
    public String showFormForRegister(Model model) {

        model.addAttribute("player", new DtoPlayer());
        model.addAttribute("skillLevels", playerConfig.SKILL_LEVELS);
        model.addAttribute("preferredGameTypes", playerConfig.GAME_TYPES);
        model.addAttribute("playStyles", playerConfig.PLAYER_TYPES);

        return "players/player-form";

    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("playerId") int id, Model model) {
        // get the player from the service
        Player player = playerService.findById(id);
        doUpdate(player, model);

        //send over the form
        return "players/player-form";
    }

    @GetMapping("/showFormForCurrentUserUpdate")
    public String showFormForCurrentUserUpdate(HttpServletRequest request, Model model) {
        // Retrieve the player object from the session
        Player player = (Player) request.getSession().getAttribute("player");

        if (player != null) {
            doUpdate(player, model);
        }


        return "players/player-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("playerId") int id) {
        // delete the player
        playerService.deleteById(id);
        //redirect to the /players/list
        return "redirect:/players/list";
    }

    @PostMapping("/save")
    public String savePlayer(@Valid @ModelAttribute("player") DtoPlayer dtoPlayer,
                             BindingResult bindingResult,
                             HttpSession session, Model model) {
        logger.info("Processing Player form for: " + dtoPlayer.getUsername());

        // form validation
        if (bindingResult.hasErrors()) {
            addData(model);
            return "players/player-form";
        }

        // check the database if username already exists
        Player player = playerService.findByPlayerName(dtoPlayer.getUsername());
        //if the is not being created or the player is being updated and is not getting the same old name
        if ((player != null) && (player.getId() != dtoPlayer.getId())) {
            model.addAttribute("player", dtoPlayer);
            addData(model);
            model.addAttribute("formError", "User name already exists.");

            logger.warning("User name already exists.");
            return "players/player-form";
        }

        //save the player
        playerService.save(dtoPlayer);

        // place player in the web http session for later use
        Player loggedInPlayer = (Player) session.getAttribute("player");
        if (loggedInPlayer != null) {
            if (dtoPlayer.getId() == loggedInPlayer.getId()) {
                // DTO player ID matches the logged-in player ID
                session.setAttribute("player", playerService.findById(dtoPlayer.getId()));
            }
        }
        //check if it is a registering user or not
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Registering user or anonymous user
            return "registration-confirmation";
        } else {
            // Logged-in user
            return "redirect:/players/list";
        }


    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    public void transferData(Player player, DtoPlayer dtoPlayer) {
        dtoPlayer.setId(player.getId());
        dtoPlayer.setUsername(player.getUsername());
        dtoPlayer.setPassword(player.getPassword());
        dtoPlayer.setPlayStyle(player.getPlayStyle());
        dtoPlayer.setSkillLevel(player.getSkillLevel());
        dtoPlayer.setPreferredGameType(player.getPreferredGameType());
    }

    public void addData(Model model) {
        model.addAttribute("skillLevels", playerConfig.SKILL_LEVELS);
        model.addAttribute("preferredGameTypes", playerConfig.GAME_TYPES);
        model.addAttribute("playStyles", playerConfig.PLAYER_TYPES);
    }

    private void doUpdate(Player player, Model model) {
        //transfer the data to the dto
        DtoPlayer dtoPlayer = new DtoPlayer();
        transferData(player, dtoPlayer);
        //set player in the model to precalculate the form

        model.addAttribute("player", dtoPlayer);
        addData(model);


        // Pre-selected game types (split into a list)
        List<String> preselectedGameTypes = Collections.singletonList(player.getPreferredGameType());
        if (preselectedGameTypes.get(0) != null) {
            // Safe to split here
            preselectedGameTypes = Arrays.asList(preselectedGameTypes.get(0).split(","));
        } else {
            preselectedGameTypes = new ArrayList<>();  // Initialize empty list if null
        }

        model.addAttribute("preselectedGameTypes", preselectedGameTypes);


    }

    @GetMapping("/showRatings")
    public String showRatings(@RequestParam("playerId") int playerId, Model model) {
        List<GameRatings> gameRatingsList = gameRatingsService.getGameRatingsForPlayer(playerId);
        Player player = playerService.findById(playerId);
        model.addAttribute("player", player);
        model.addAttribute("gameRatings", gameRatingsList);
        return "players/ratings-display";
    }


}




