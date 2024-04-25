package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.GameSession;
import com.methodus.gamenightmetricsapp.entity.GameSessionPlayerPk;
import com.methodus.gamenightmetricsapp.entity.SessionWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionWinnerRepository extends JpaRepository<SessionWinner, GameSessionPlayerPk> {
    List<SessionWinner> findAllByGameSession(Optional<GameSession> gameSession);
}

