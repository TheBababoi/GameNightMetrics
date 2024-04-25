package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.BoardGameRepository;
import com.methodus.gamenightmetricsapp.entity.BoardGame;
import com.methodus.gamenightmetricsapp.entity.DtoBoardGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardGameServiceImpl implements BoardGameService {
    private final BoardGameRepository boardGameRepository;

    @Autowired
    public BoardGameServiceImpl(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    @Override
    public List<BoardGame> findAll() {
        return boardGameRepository.findAllByOrderByNameAsc();
    }

    @Override
    public BoardGame findById(int id) {
        Optional<BoardGame> result = boardGameRepository.findById(id);
        BoardGame boardGame;
        if (result.isPresent()) {
            boardGame = result.get();
        } else {
            // we didn't find the boardgame
            throw new RuntimeException("Did not find boardgame id - " + id);
        }
        return boardGame;
    }

    @Override
    public BoardGame save(DtoBoardGame dtoBoardGame) {
        BoardGame boardGame;
        //check if new boardgame or boardgame update
        //if update it will already have id
        if (dtoBoardGame.getId() == 0) {
            boardGame = new BoardGame();
        } else {
            boardGame = findById(dtoBoardGame.getId());
        }

        //transfer the data back to the entity
        boardGame.copyFromDto(dtoBoardGame);
        return boardGameRepository.save(boardGame);
    }


    @Override
    public void deleteById(int id) {
        boardGameRepository.deleteById(id);
    }

    @Override
    public BoardGame findByBoardGameName(String name) {
        return boardGameRepository.findBoardGameByName(name);
    }
}
