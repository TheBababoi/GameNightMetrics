package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
    List<GameSession> findAllByGameId(int gameId);

    GameSession findById(int id);
}

