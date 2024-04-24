package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.PlayerGameStats;
import com.methodus.gamenightmetricsapp.entity.PlayerGameStatsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerGameStatsRepository extends JpaRepository<PlayerGameStats, Integer> {



    Optional<PlayerGameStats> findById(PlayerGameStatsPK pk);


    @Query("SELECT p FROM PlayerGameStats p WHERE p.boardGame.id = :id ORDER BY p.wins DESC, p.winLossRatio DESC")
    List<PlayerGameStats> findByBoardGameId(@Param("id") int id);

    @Query("SELECT p FROM PlayerGameStats p WHERE p.player.id = :id ORDER BY p.wins DESC, p.winLossRatio DESC")
    List<PlayerGameStats> findByPlayerId(@Param("id") int id);

    @Query("SELECT p.player, SUM(p.wins) AS totalWins, SUM(p.loses) AS totalLosses, SUM(p.plays) AS totalPlays, AVG(p.winLossRatio) AS averageWinLossRatio " +
            "FROM PlayerGameStats p GROUP BY p.player.id " +
            "ORDER BY totalWins DESC, totalLosses ASC")
    List<Object[]> findLeaderboardStats();


    @Query("SELECT p.player, SUM(p.wins) AS totalWins, SUM(p.loses) AS totalLosses, SUM(p.plays) AS totalPlays, AVG(p.winLossRatio) AS averageWinLossRatio " +
            "FROM PlayerGameStats p " +
            "WHERE LOCATE(:gameType, p.boardGame.gameType) > 0 " +
            "GROUP BY p.player.id " +
            "ORDER BY totalWins DESC, totalLosses ASC")
    List<Object[]> findLeaderboardStatsByGameType(String gameType);


    void deleteById(PlayerGameStatsPK id);
}
