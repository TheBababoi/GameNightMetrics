package com.methodus.gamenightmetricsapp.security;

import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.service.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final PlayerService playerService;

    public CustomAuthenticationSuccessHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String playerName = authentication.getName();
        Player player = playerService.findByPlayerName(playerName);
        HttpSession session = request.getSession();
        session.setAttribute("player", player);
        response.sendRedirect(request.getContextPath() + "/home");

    }
}
