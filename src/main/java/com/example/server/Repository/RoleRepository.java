package com.example.server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    
    Role findByRole(String role);
}
