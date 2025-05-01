package com.example.server.entity;

import lombok.Data;

@Data
public class UserDto {
    
    private String username;
    private String password;
    private String role;
}
