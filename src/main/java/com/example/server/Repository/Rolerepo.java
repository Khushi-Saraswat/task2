package com.example.server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.Role;

public interface Rolerepo extends JpaRepository<Role,Long>{
 
    Role findByRole(String role);
}
