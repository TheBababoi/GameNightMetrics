package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.GameRatings;

import java.util.List;

public interface GameRatingsService {
    GameRatings save(GameRatings gameRatings);

    List<GameRatings> getGameRatingsForBoardgame(int boardgameId);
    List<GameRatings> getGameRatingsForPlayer(int playerId);
}
