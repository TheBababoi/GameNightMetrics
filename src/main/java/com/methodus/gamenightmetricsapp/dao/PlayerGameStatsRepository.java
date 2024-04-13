package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameStatsRepository extends JpaRepository<PlayerGameStats, PlayerGameStatsPK> {

}