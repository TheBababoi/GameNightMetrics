package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="players")
public class Player {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="skill_level")
    private String skillLevel;
    @Column(name="play_style")
    private String playStyle;
    @Column(name="preferred_game_type")
    private String preferredGameType;
    @Column(name="role_id")
    private String roleID;

    // define constructs


    public Player() {
    }

    public Player(String username, String password, String skillLevel, String playStyle, String preferredGameType) {
        this.username = username;
        this.password = password;
        this.skillLevel = skillLevel;
        this.playStyle = playStyle;
        this.preferredGameType = preferredGameType;
    }

    // define getters/setters


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

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    // define toString


    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", playStyle='" + playStyle + '\'' +
                ", preferredGameType='" + preferredGameType + '\'' +
                ", roleID='" + roleID + '\'' +
                '}';
    }
}
