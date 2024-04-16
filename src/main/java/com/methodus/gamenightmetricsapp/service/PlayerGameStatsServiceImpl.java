package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.PlayerGameStatsRepository;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerGameStatsServiceImpl implements  PlayerGameStatsService {
    private PlayerGameStatsRepository playerGameStatsRepository;

    @Autowired
    public PlayerGameStatsServiceImpl(PlayerGameStatsRepository playerGameStatsRepository) {
        this.playerGameStatsRepository = playerGameStatsRepository;
    }

    @Override
    public Optional<PlayerGameStats> findById(PlayerGameStatsPK pk) {
        return playerGameStatsRepository.findById(pk);
    }

    @Override
    public List<PlayerGameStats> saveAll(List<PlayerGameStats> playerGameStatsList) {
        return playerGameStatsRepository.saveAll(playerGameStatsList);
    }

    @Override
    public List<PlayerGameStats> getPlayerStatsForBoardGame(int boardgameId) {
        return playerGameStatsRepository.findByBoardGameId(boardgameId);
    }

    @Override
    public List<PlayerGameStats> getPlayerStatsForPlayer(int playerId) {
        return  playerGameStatsRepository.findByPlayerId(playerId);
    }

    @Override
    public List<Object[]> getLeaderboardStats() {
        return  playerGameStatsRepository.findLeaderboardStats();

    }


}
