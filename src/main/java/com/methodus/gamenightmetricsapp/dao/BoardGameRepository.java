package com.methodus.gamenightmetricsapp.dao;

import com.methodus.gamenightmetricsapp.entity.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame,Integer> {
    public List<BoardGame> findAllByOrderByNameAsc();
}
