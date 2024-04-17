package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameRatingsRepository;
import com.methodus.gamenightmetricsapp.entity.GameRatings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
