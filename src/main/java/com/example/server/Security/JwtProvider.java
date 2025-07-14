package com.example.server.Security;

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

        var authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .claim("email", auth.getName())
                .claim("roles", authorityList)
                .signWith(JwtConstant.SECRET_KEY, Jwts.SIG.HS384) // ✅ specify algorithm
                .compact();
    }

    public static String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key) // assumes `key` is the same used during signing
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class); // ✅ you're extracting correct custom claim
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
        Object rolesObject = claims.get("roles");

        if (rolesObject instanceof Collection<?>) {
            return ((Collection<?>) rolesObject).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }

        return new HashSet<>();
    }
}
