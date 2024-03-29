package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.BoardGame;

import java.util.List;

public interface BoardGameService {
    List<BoardGame> findAll();
    BoardGame findById(int id);
    BoardGame save(BoardGame boardGame);
    void deleteById(int id);
}
