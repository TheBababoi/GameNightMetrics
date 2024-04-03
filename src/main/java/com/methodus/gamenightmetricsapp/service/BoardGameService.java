package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.DtoBoardGame;
import com.methodus.gamenightmetricsapp.entity.DtoPlayer;

import java.util.List;

public interface BoardGameService {
    List<BoardGame> findAll();
    BoardGame findById(int id);
    BoardGame save(DtoBoardGame dtoBoardGame);
    void deleteById(int id);
    BoardGame findByBoardGameName(String name);
}
