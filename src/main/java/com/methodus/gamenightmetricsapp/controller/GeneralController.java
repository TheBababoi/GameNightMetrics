package com.methodus.gamenightmetricsapp.controller;

import com.methodus.gamenightmetricsapp.entity.Player;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
    @GetMapping("/")
    public String showLanding() {
        return "landing";
    }

    @GetMapping("/home")
    public String showHome(HttpServletRequest request, Model model) {
        Player player = (Player) request.getSession().getAttribute("player");
        model.addAttribute("username", player.getUsername());
        return "home";
    }

}
