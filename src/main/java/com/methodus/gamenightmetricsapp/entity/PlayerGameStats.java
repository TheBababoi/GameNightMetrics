package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "player_game_stats")
public class PlayerGameStats {

    @Id
    @EmbeddedId
    private PlayerGameStatsPK id;  // Embeddable class as the primary key

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private BoardGame boardGame;

    @Column(name = "wins")
    private int wins;

    @Column(name = "loses")
    private int loses;

    @Column(name = "plays")
    private int plays;

    @Column(name = "win_loss_ratio")
    private double winLossRatio;

    public PlayerGameStatsPK getId() {
        return id;
    }

    public void setId(PlayerGameStatsPK id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Optional<BoardGame> getBoardGame() {
        return Optional.ofNullable(boardGame);
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public double getWinLossRatio() {
        return winLossRatio;
    }

    public void setWinLossRatio(double winLossRatio) {
        this.winLossRatio = winLossRatio;
    }



    public void setBoardGame(Optional<BoardGame> boardGame) {
    }
}

