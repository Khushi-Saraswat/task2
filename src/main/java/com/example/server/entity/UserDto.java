package com.example.server.entity;

import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String email;
    private String password;
    private String role;
}
