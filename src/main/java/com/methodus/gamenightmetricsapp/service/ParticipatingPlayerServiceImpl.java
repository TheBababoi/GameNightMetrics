package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameSessionRepository;
import com.methodus.gamenightmetricsapp.dao.ParticipatingPlayerRepository;
import com.methodus.gamenightmetricsapp.entity.ParticipatingPlayer;
import com.methodus.gamenightmetricsapp.entity.SessionWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ParticipatingPlayerServiceImpl implements ParticipatingPlayerService{

    private ParticipatingPlayerRepository playerRepository;
    private GameSessionRepository gameSessionRepository;

    @Autowired
    public ParticipatingPlayerServiceImpl(ParticipatingPlayerRepository playerRepository, GameSessionRepository gameSessionRepository) {
        this.playerRepository = playerRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public List<ParticipatingPlayer> saveAll(List<ParticipatingPlayer> participatingPlayers) {
      return  playerRepository.saveAll(participatingPlayers);
    }

    @Override
    public List<ParticipatingPlayer> findByGameSession_Id(int gameSessionId) {
        return playerRepository.findAllByGameSession(Optional.ofNullable(gameSessionRepository.findById(gameSessionId)));
    }



}
