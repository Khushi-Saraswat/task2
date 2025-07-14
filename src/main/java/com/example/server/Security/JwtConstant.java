package com.example.server.Security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

public class JwtConstant {
    private static final String SECRET = "your-super-long-static-secret-key-for-jwt-signing"; // min 48 bytes

    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); // HS384-compatible

    public static final String JWT_HEADER = "Authorization";
}
