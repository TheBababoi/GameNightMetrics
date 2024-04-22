package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "session_winners")
public class SessionWinner {

    @EmbeddedId
    private GameSessionPlayerPk id;


    @ManyToOne
    @JoinColumn(name = "player_id", insertable=false, updatable=false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private GameSession gameSession;

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
