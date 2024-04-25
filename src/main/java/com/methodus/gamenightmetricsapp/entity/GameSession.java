package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_session")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private int id;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "session_date", nullable = false)
    private Date sessionDate;

    @Column(name = "session_duration", nullable = false)
    private String sessionDuration;

    @Column(name = "total_players", nullable = false)
    private int totalPlayers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BoardGame boardGame;


    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private List<ParticipatingPlayer> participatingPlayers = new ArrayList<>();

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private List<SessionWinner> sessionWinners = new ArrayList<>();

    public GameSession() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(String sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

}
