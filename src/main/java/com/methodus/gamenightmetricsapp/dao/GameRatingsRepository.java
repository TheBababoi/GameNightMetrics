package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.GameRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GameRatingsRepository extends JpaRepository<GameRatings,Integer> {

    @Query("SELECT gr FROM GameRatings gr JOIN gr.player p WHERE gr.boardGame.id = :boardgameId")
    List<GameRatings> findByBoardGameId(@RequestParam("boardgameId") int boardgameId);


    @Query("SELECT gr FROM GameRatings gr JOIN gr.boardGame p WHERE gr.player.id = :playerId")
    List<GameRatings> findByPlayerId(@RequestParam("playerId") int playerId);
}



