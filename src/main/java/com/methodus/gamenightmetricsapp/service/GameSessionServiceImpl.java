package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameSessionRepository;
import com.methodus.gamenightmetricsapp.entity.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionServiceImpl implements GameSessionService{
    private GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public GameSession save(GameSession gameSession) {
         return gameSessionRepository.save(gameSession);
    }

    @Override
    public List<GameSession> findAll() {
        return gameSessionRepository.findAll();
    }
}
