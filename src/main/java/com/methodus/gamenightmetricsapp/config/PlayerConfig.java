package com.methodus.gamenightmetricsapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerConfig {
    public final String[] SKILL_LEVELS = {"Newcomer", "Enthusiast", "Developing Strategist", "Experienced Gamer", "Board Game Guru"};
    public final String[] GAME_TYPES = {"Strategy", "Role-Playing", "Party", "Abstract", "Mystery", "Social Deduction", "Push your Luck", "Family", "Dexterity", "Card"};
    public final String[] PLAYER_TYPES = {"Aggressive Dominator", "Passive Observer", "Balanced Strategist", "Astute Diplomat", "Wildcard"};

}
