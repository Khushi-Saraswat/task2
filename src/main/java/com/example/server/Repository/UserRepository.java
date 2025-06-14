package com.example.server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);

    boolean existsByEmail(String email);

}
