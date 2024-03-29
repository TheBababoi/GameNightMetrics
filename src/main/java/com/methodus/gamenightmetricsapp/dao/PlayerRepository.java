package com.methodus.gamenightmetricsapp.dao;


import com.methodus.gamenightmetricsapp.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    public List<Player> findAllByOrderByUsernameAsc();
}
