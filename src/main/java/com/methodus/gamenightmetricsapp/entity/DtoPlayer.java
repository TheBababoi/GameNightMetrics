package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//data transfer object class
public class DtoPlayer {

    private int id;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String username;
    @NotNull(message = "Password is required")
    @Size(min = 4, max = 40, message = " must be between 4 and 40 characters long")
    private String password;
    @Column(name = "skill_level")
    private String skillLevel;
    @Column(name = "play_style")
    private String playStyle;
    @Column(name = "preferred_game_type")
    private String preferredGameType;
    private int totalGamesPlayed;


    public DtoPlayer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getPlayStyle() {
        return playStyle;
    }

    public void setPlayStyle(String playStyle) {
        this.playStyle = playStyle;
    }

    public String getPreferredGameType() {
        return preferredGameType;
    }

    public void setPreferredGameType(String preferredGameType) {
        this.preferredGameType = preferredGameType;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public void copyFromPlayer(Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
        this.password = player.getPassword();
        this.playStyle = player.getPlayStyle();
        this.preferredGameType = player.getPreferredGameType();
        this.skillLevel = player.getSkillLevel();
        this.totalGamesPlayed = player.getTotalGamesPlayed();
    }

    @Override
    public String toString() {
        return "DtoPlayer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", playStyle='" + playStyle + '\'' +
                ", preferredGameType='" + preferredGameType + '\'' +
                ", totalGamesPlayed=" + totalGamesPlayed +
                '}';
    }
}
