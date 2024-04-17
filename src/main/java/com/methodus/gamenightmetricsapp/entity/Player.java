package com.methodus.gamenightmetricsapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;




import java.util.Collection;

@Entity
@Table(name ="player")
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
    @Column(name = "total_games_played")
    private int totalGamesPlayed;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "players_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "player_game_stats",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Collection<BoardGame> boardgames;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "game_ratings",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Collection<BoardGame> boardgameratings;



    // define constructors


    public Player() {
    }

    public Player(String username, String password, String skillLevel, String playStyle, String preferredGameType) {
        this.username = username;
        this.password = password;
        this.skillLevel = skillLevel;
        this.playStyle = playStyle;
        this.preferredGameType = preferredGameType;
    }

    public Player(String username, String password, String skillLevel, String playStyle, String preferredGameType, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.skillLevel = skillLevel;
        this.playStyle = playStyle;
        this.preferredGameType = preferredGameType;
        this.roles = roles;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
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
                '}';
    }

    public void copyFromDto(DtoPlayer dtoPlayer, PasswordEncoder passwordEncoder) {
        this.setId(dtoPlayer.getId());
        this.setUsername(dtoPlayer.getUsername());

        if (dtoPlayer.getPassword().length() >= 59) {
            // Likely encrypted
            this.setPassword(dtoPlayer.getPassword());
        } else {
            // Likely not encrypted
            this.setPassword(passwordEncoder.encode(dtoPlayer.getPassword()));
        }
        this.setPlayStyle(dtoPlayer.getPlayStyle());
        this.setSkillLevel(dtoPlayer.getSkillLevel());
        this.setPreferredGameType(dtoPlayer.getPreferredGameType());
        this.setTotalGamesPlayed(dtoPlayer.getTotalGamesPlayed());
    }
}
