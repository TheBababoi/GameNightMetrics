package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.PlayerGameStatsRepository;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;
import jakarta.transaction.Transactional;
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
    public Optional<PlayerGameStats>  findById(PlayerGameStatsPK pk) {
        return playerGameStatsRepository.findById(pk);
    }


    @Override
    public void saveAll(List<PlayerGameStats> playerGameStatsList) {
        playerGameStatsRepository.saveAll(playerGameStatsList);
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
    public List<Object[]> getLeaderboardStats(String gameType) {
        if (gameType != null && !gameType.isEmpty()) {
            // Call  repository method with game type filtering
            return playerGameStatsRepository.findLeaderboardStatsByGameType(gameType);
        } else {
            // Call repository method to retrieve all games
            return playerGameStatsRepository.findLeaderboardStats();
        }

    }

    @Override
    @Transactional
    public void delete(PlayerGameStatsPK id) {
        playerGameStatsRepository.deleteById(id);
    }

    @Override
    public void save(PlayerGameStats playerGameStats) {
        playerGameStatsRepository.save(playerGameStats);
    }


}
