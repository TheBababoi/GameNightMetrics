package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;

import java.util.List;
import java.util.Optional;


public interface PlayerGameStatsService {

    Optional<PlayerGameStats> findById(PlayerGameStatsPK pk);

    List<PlayerGameStats> saveAll(List<PlayerGameStats> playerGameStatsList);

    List<PlayerGameStats> getPlayerStatsForBoardGame(int boardgameId);

    List<PlayerGameStats> getPlayerStatsForPlayer(int playerId);


}
