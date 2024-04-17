package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.GameRatings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRatingsRepository extends JpaRepository<GameRatings,Integer> {

}
