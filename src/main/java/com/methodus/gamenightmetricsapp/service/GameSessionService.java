package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.GameSession;

import java.util.List;

public interface GameSessionService {
    GameSession save(GameSession gameSession);

    GameSession findById(int id);

    List<GameSession> getGameSessions(int boardGameId);


    void delete(int id);
}
