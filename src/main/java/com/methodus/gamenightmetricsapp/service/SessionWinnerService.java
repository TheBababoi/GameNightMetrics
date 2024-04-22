package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.ParticipatingPlayer;
import com.methodus.gamenightmetricsapp.entity.SessionWinner;

import java.util.List;

public interface SessionWinnerService {
    List<SessionWinner> saveAll(List<SessionWinner> sessionWinnersList);

    List<SessionWinner> findByGameSession_Id(int gameSessionId);
}
