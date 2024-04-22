package com.methodus.gamenightmetricsapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardGameConfig {
    public final String[] GAME_TYPES = {"Strategy","Role-Playing","Party","Abstract","Mystery","Social Deduction","Push your Luck","Family","Dexterity","Card"};
    public final int[]  PLAYER_NUMBERS = {2,3,4,5,6,7,8,9,10};
    public final String[] GAME_TIME = {"15 Minutes","30 Minutes","45 Minutes","1 Hour","1.5 Hour","2 Hours","3 Hours","> 3 Hours"};
}
