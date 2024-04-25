package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.Column;

import java.io.Serializable;

public class GameSessionPlayerPk implements Serializable {

    @Column(name = "player_id")
    private int playerId;

    @Column(name = "session_id")
    private int sessionId;

    public GameSessionPlayerPk() {
    }

    public GameSessionPlayerPk(int playerId, int sessionId) {
        this.playerId = playerId;
        this.sessionId = sessionId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


}