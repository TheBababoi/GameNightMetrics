package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.GameRatings;
import com.methodus.gamenightmetricsapp.entity.GameSession;

import java.util.List;

public interface GameSessionService {
    GameSession save(GameSession gameSession);

    List<GameSession> findAll();
    GameSession findById(int id);
    public List<GameSession> getGameSessions(int boardGameId);


    void delete(int id);
}
