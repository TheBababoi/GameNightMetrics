package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


//data transfer object class
public class DtoBoardGame {

    private int id;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String name;
    private String gameType;
    private int maxPlayers;
    private int minPlayers;
    private int totalGamesPlayed;
    private double averageTotalRating;
    private int averageDifficultyRating;
    private int numberOfRatings;

    public DtoBoardGame() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public double getAverageTotalRating() {
        return averageTotalRating;
    }

    public void setAverageTotalRating(double averageTotalRating) {
        this.averageTotalRating = averageTotalRating;
    }

    public int getAverageDifficultyRating() {
        return averageDifficultyRating;
    }

    public void setAverageDifficultyRating(int averageDifficultyRating) {
        this.averageDifficultyRating = averageDifficultyRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void copyFromBoardGame(BoardGame boardGame) {
        this.id = boardGame.getId();
        this.name = boardGame.getName();
        this.minPlayers = boardGame.getMinPlayers();
        this.maxPlayers = boardGame.getMaxPlayers();
        this.gameType = boardGame.getGameType();
        this.totalGamesPlayed = boardGame.getTotalGamesPlayed();
        this.averageTotalRating = boardGame.getAverageTotalRating();
        this.averageDifficultyRating = boardGame.getAverageDifficultyRating();
        this.numberOfRatings = boardGame.getNumberOfRatings();
    }

    @Override
    public String toString() {
        return "DtoBoardGame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gameType='" + gameType + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", minPlayers=" + minPlayers +
                ", totalGamesPlayed=" + totalGamesPlayed +
                '}';
    }
}
