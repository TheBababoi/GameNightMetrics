package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.PlayerRepository;
import com.methodus.gamenightmetricsapp.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PlayerServiceImpl implements PlayerService{
    PlayerRepository playerRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> findAll() {

        return (List<Player>)  playerRepository.findAllByOrderByUsernameAsc();
    }

    @Override
    public Player findById(int id) {
        Optional<Player> result = playerRepository.findById(id);
        Player player;
        if (result.isPresent()){
            player=result.get();
        } else {
            // we didn't find the player id
            throw new RuntimeException("Did not find player id - " + id);
        }
        return player;
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(int id) {
        playerRepository.deleteById(id);
    }
}
