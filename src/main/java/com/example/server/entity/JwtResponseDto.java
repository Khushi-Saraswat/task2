package com.example.server.entity;

import java.util.List;

public class JwtResponseDto {
    private String token;
    private String type = "Bearer";
    private String userId;

    private String email;
    private List<String> roles;
    private String status;

    public JwtResponseDto(String accessToken, String userId, String email, List<String> roles) {
        this.token = accessToken;
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

}
