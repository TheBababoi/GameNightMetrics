package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "game_ratings")
public class GameRatings {


    @EmbeddedId
    private GameRatingsPK id;

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private BoardGame boardGame;

    @Column(name = "total_rating")
    private int totalRating;

    @Column(name = "gameplay_rating")
    private int gameplayRating;

    @Column(name = "theme_rating")
    private int themeRating;

    @Column(name = "visual_rating")
    private int visualRating;
    @Column(name = "difficulty_rating")
    private int difficultyRating;

    @Column(name = "comment")
    private String comment;

    public GameRatings() {
    }

    public GameRatings(GameRatingsPK id, int totalRating, int gameplayRating, int themeRating, int visualRating, int difficultyRating, String comment) {
        this.id = id;
        this.totalRating = totalRating;
        this.gameplayRating = gameplayRating;
        this.themeRating = themeRating;
        this.visualRating = visualRating;
        this.difficultyRating = difficultyRating;
        this.comment = comment;
    }

    public GameRatingsPK getId() {
        return id;
    }

    public void setId(GameRatingsPK id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public int getGameplayRating() {
        return gameplayRating;
    }

    public void setGameplayRating(int gameplayRating) {
        this.gameplayRating = gameplayRating;
    }

    public int getThemeRating() {
        return themeRating;
    }

    public void setThemeRating(int themeRating) {
        this.themeRating = themeRating;
    }

    public int getVisualRating() {
        return visualRating;
    }

    public void setVisualRating(int visualRating) {
        this.visualRating = visualRating;
    }

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

