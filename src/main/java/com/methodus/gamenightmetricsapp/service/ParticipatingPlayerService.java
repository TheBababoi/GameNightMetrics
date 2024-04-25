package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.ParticipatingPlayer;

import java.util.List;

public interface ParticipatingPlayerService {
    void saveAll(List<ParticipatingPlayer> participatingPlayers);

    List<ParticipatingPlayer> findByGameSession_Id(int gameSessionId);
}
