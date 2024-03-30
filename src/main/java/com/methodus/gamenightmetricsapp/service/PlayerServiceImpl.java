package com.methodus.gamenightmetricsapp.service;

import com.methodus.gamenightmetricsapp.dao.PlayerRepository;
import com.methodus.gamenightmetricsapp.dao.RoleRepository;
import com.methodus.gamenightmetricsapp.entity.Player;
import com.methodus.gamenightmetricsapp.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService{
    private PlayerRepository playerRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public List<Player> findAll() {

        return playerRepository.findAllByOrderByUsernameAsc();
    }

    @Override
    public Player findById(int id) {
        Optional<Player> result = playerRepository.findById(id);
        Player player;
        if (result.isPresent()){
            player=result.get();
        } else {
            // we didn't find the player id
            throw new RuntimeException("Did not find player id - " + id);
        }
        return player;
    }

    @Override
    public Player save(Player player) {
        //check if the player does not have roles
        if (player.getRoles()==null){
            System.out.println("hell yeah");
            //set player role to user
            player.setRoles(Collections.singletonList(roleRepository.findRoleByName("ROLE_USER")));
        }
        player.setPassword(bCryptPasswordEncoder.encode(player.getPassword()));
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(int id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Player findByPlayerName(String userName) {
        return playerRepository.findPlayerByUsername(userName);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findPlayerByUsername(username);
        if(player==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(player.getUsername(), player.getPassword(),
                mapRolesToAuthorities(player.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
