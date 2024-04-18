package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameRatingsRepository;
import com.methodus.gamenightmetricsapp.entity.GameRatings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameRatingsServiceImp implements GameRatingsService{
    private GameRatingsRepository gameRatingsRepository;
    @Autowired
    public GameRatingsServiceImp(GameRatingsRepository gameRatingsRepository) {
        this.gameRatingsRepository = gameRatingsRepository;
    }

    @Override
    public GameRatings save(GameRatings gameRatings) {
        return gameRatingsRepository.save(gameRatings);
    }

    @Override
    public List<GameRatings> getGameRatingsForBoardgame(int boardgameId) {
        return gameRatingsRepository.findByBoardGameId(boardgameId);
    }

    @Override
    public List<GameRatings> getGameRatingsForPlayer(int playerId) {
        return gameRatingsRepository.findByPlayerId(playerId);
    }
}
