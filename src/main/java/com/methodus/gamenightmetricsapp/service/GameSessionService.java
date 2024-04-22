package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.GameSession;

import java.util.List;

public interface GameSessionService {
    GameSession save(GameSession gameSession);

    List<GameSession> findAll();
}
