package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PlayerGameStatsPK implements Serializable {

    @Column(name = "player_id")
    private int playerId;

    @Column(name = "game_id")
    private int gameId;

    public PlayerGameStatsPK() {
    }

    public PlayerGameStatsPK(int playerId, int gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
