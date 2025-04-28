package com.example.server.Security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class JwtConstant {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS384.key().build(); // Correct way in JJWT 0.12.0+
    public static final String JWT_HEADER = "Authorization";
}
