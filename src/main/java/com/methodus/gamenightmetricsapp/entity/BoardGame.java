package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name="boardgame")
public class BoardGame {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="game_type")
    private String gameType;
    @Column(name="number_of_max_players")
    private int maxPlayers;
    @Column(name="number_of_min_players")
    private int minPlayers;
    @Column(name="total_games_played")
    private int totalGamesPlayed;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "player_game_stats",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Collection<Player> players;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "game_ratings",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Collection<Player> gameratings;

    public BoardGame() {
    }

    public BoardGame(String name, String gameType, int maxPlayers, int minPlayers) {
        this.name = name;
        this.gameType = gameType;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
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

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

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

    public void copyFromDto(DtoBoardGame dtoBoardGame) {
        this.setId(dtoBoardGame.getId());
        this.setName(dtoBoardGame.getName());
        this.setGameType(dtoBoardGame.getGameType());
        this.setMinPlayers(dtoBoardGame.getMinPlayers());
        this.setMaxPlayers(dtoBoardGame.getMaxPlayers());
        this.setTotalGamesPlayed(dtoBoardGame.getTotalGamesPlayed());
    }
}
