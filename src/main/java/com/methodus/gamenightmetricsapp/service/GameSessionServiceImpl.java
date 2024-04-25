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
    public GameSession findById(int id) {
        return gameSessionRepository.findById(id);
    }

    @Override
    public List<GameSession> getGameSessions(int boardGameId) {
        if (boardGameId!=0) {
            return gameSessionRepository.findAllByGameId(boardGameId);
        } else {
            return gameSessionRepository.findAll();
        }

    }

    @Override
    public void delete(int id) {
        gameSessionRepository.deleteById(id);
    }
}
