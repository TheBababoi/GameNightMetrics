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
    @NotNull(message = "is required")
    @Size(min = 4, message = "must be at least 4 characters long")
    private String password;
    @Column(name="skill_level")
    private String skillLevel;
    @Column(name="play_style")
    private String playStyle;
    @Column(name="preferred_game_type")
    private String preferredGameType;




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
}
