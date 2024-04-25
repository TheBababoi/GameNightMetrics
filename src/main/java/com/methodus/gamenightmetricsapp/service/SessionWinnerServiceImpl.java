package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.GameSessionRepository;
import com.methodus.gamenightmetricsapp.dao.SessionWinnerRepository;
import com.methodus.gamenightmetricsapp.entity.SessionWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionWinnerServiceImpl implements SessionWinnerService {

    private final SessionWinnerRepository sessionWinnerRepository;
    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public SessionWinnerServiceImpl(SessionWinnerRepository sessionWinnerRepository, GameSessionRepository gameSessionRepository) {
        this.sessionWinnerRepository = sessionWinnerRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public void saveAll(List<SessionWinner> sessionWinnersList) {
        sessionWinnerRepository.saveAll(sessionWinnersList);
    }

    @Override
    public List<SessionWinner> findByGameSession_Id(int gameSessionId) {
        return sessionWinnerRepository.findAllByGameSession(Optional.ofNullable(gameSessionRepository.findById(gameSessionId)));
    }
}
