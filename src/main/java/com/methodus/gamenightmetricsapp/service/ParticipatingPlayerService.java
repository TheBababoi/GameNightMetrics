package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.ParticipatingPlayer;
import com.methodus.gamenightmetricsapp.entity.SessionWinner;

import java.util.List;

public interface ParticipatingPlayerService {
    List<ParticipatingPlayer> saveAll(List<ParticipatingPlayer> participatingPlayers);

    List<ParticipatingPlayer> findByGameSession_Id(int gameSessionId);
}
