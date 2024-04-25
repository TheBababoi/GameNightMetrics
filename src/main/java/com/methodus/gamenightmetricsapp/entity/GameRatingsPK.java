package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.Column;

import java.io.Serializable;

public class GameRatingsPK implements Serializable {

    @Column(name = "player_id")
    private int playerId;

    @Column(name = "game_id")
    private int gameId;

    public GameRatingsPK() {
    }

    public GameRatingsPK(int playerId, int gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
