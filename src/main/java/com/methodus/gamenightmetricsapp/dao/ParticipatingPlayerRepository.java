package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.GameSession;
import com.methodus.gamenightmetricsapp.entity.GameSessionPlayerPk;
import com.methodus.gamenightmetricsapp.entity.ParticipatingPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipatingPlayerRepository extends JpaRepository<ParticipatingPlayer, GameSessionPlayerPk> {
    public List<ParticipatingPlayer> findAllByGameSession(Optional<GameSession> gameSession);

}


