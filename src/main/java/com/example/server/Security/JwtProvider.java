package com.example.server.Security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtProvider {
    private static final SecretKey key = JwtConstant.SECRET_KEY; // Use directly

    public static String generateToken(Authentication auth) {

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Generate the JWT token
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .claim("email", auth.getName()) // Store email (username or email based on your setup)
                .claim("roles", roles) // Store roles as a string (comma-separated if more than one role)
                .signWith(JwtConstant.SECRET_KEY) // Use the correct key from JwtConstant
                .compact();
    }

    public static String getEmailFromToken(String token) {
        token = token.substring(7); // Remove "Bearer " prefix
        Claims claims = Jwts.parser()
                .verifyWith(key) // Use correct verification method
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class);
    }

    public static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authSet = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            authSet.add(ga.getAuthority());
        }
        return String.join(",", authSet);
    }

    public static Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;

    }

    public static Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        String rolesString = claims.get("roles", String.class);

        if (rolesString == null || rolesString.isEmpty()) {
            return new HashSet<>();
        }

        String[] rolesArray = rolesString.split(",");
        return new HashSet<>(Arrays.asList(rolesArray));
    }

}
