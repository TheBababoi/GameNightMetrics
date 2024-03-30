package com.methodus.gamenightmetricsapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class GeneralController {
    @GetMapping("/")
    public String showLanding() {
        return "landing";
    }

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }
}
