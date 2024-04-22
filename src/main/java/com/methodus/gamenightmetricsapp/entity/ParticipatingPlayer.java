package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "participating_players")
public class ParticipatingPlayer {


    @EmbeddedId
    private GameSessionPlayerPk id;


    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false,insertable=false, updatable=false)
    private GameSession gameSession;


    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false, insertable=false, updatable=false)
    private Player player;

    public GameSessionPlayerPk getId() {
        return id;
    }

    public void setId(GameSessionPlayerPk id) {
        this.id = id;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

