package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
}

