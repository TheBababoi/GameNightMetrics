package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameSessionRepository;
import com.methodus.gamenightmetricsapp.dao.SessionWinnerRepository;

import com.methodus.gamenightmetricsapp.entity.SessionWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SessionWinnerServiceImpl implements SessionWinnerService{

    private SessionWinnerRepository sessionWinnerRepository;
    private GameSessionRepository gameSessionRepository;
    @Autowired
    public SessionWinnerServiceImpl(SessionWinnerRepository sessionWinnerRepository, GameSessionRepository gameSessionRepository) {
        this.sessionWinnerRepository = sessionWinnerRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public List<SessionWinner> saveAll(List<SessionWinner> sessionWinnersList) {
       return sessionWinnerRepository.saveAll(sessionWinnersList);
    }

    @Override
    public List<SessionWinner> findByGameSession_Id(int gameSessionId) {
        return sessionWinnerRepository.findAllByGameSession(Optional.ofNullable(gameSessionRepository.findById(gameSessionId)));
    }
}
