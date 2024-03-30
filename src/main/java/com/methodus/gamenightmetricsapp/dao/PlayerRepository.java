package com.methodus.gamenightmetricsapp.dao;


import com.methodus.gamenightmetricsapp.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    public List<Player> findAllByOrderByUsernameAsc();
    public Player findPlayerByUsername(String username);

}
