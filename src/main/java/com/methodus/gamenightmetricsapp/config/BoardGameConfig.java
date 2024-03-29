package com.methodus.gamenightmetricsapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardGameConfig {
    public final String[] GAME_TYPES = {"Strategy","Role-Playing Game","Party Game","Abstract","Mystery","Push your Luck","Family","Dexterity","Card Game"};
    public final int[]  PLAYER_NUMBERS = {2,3,4,5,6,7,8,9,10};
}
