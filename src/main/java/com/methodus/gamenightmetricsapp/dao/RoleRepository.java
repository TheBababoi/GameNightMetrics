package com.methodus.gamenightmetricsapp.dao;


import com.methodus.gamenightmetricsapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByName(String name);

}
