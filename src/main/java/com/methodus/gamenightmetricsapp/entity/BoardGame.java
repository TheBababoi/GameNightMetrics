package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="boardgame")
public class BoardGame {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="game_type")
    private String gameType;
    @Column(name="number_of_max_players")
    private String maxPlayers;
    @Column(name="number_of_min_players")
    private String minPlayers;


    // define constructors

    public BoardGame() {
    }

    public BoardGame(String name, String gameType, String maxPlayers, String minPlayers) {
        this.name = name;
        this.gameType = gameType;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    // define getters/setters

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

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
    }


    // define toString


    @Override
    public String toString() {
        return "BoardGame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gameType='" + gameType + '\'' +
                ", maxPlayers='" + maxPlayers + '\'' +
                ", minPlayers='" + minPlayers + '\'' +
                '}';
    }
}
