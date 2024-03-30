package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.Player;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PlayerService extends UserDetailsService {
    List<Player> findAll();
    Player findById(int id);
    Player save(Player player);
    void deleteById(int id);
    Player findByPlayerName(String userName);



}
