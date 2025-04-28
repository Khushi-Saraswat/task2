package com.example.server.Security;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        String roles = populateAuthorities(authorities);

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 86400000)) // 1 day expiration
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key) // Use the correct key
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
}
