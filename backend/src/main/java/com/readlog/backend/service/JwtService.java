package com.readlog.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Reads jwt.secret from application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Reads jwt.expiration from application.properties (24 hours in ms)
    @Value("${jwt.expiration}")
    private long expiration;

    // Convert our secret string into a secure key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate a JWT token for a user
    // Token contains the user's email and expiry time
    public String generateToken(String email) {
        return Jwts.builder()
            .subject(email)                              // who this token belongs to
            .issuedAt(new Date())                        // when token was created
            .expiration(new Date(System.currentTimeMillis() + expiration)) // when it expires
            .signWith(getSigningKey())                   // sign with our secret key
            .compact();                                  // build the token string
    }

    // Extract email from a token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if token is still valid (not expired)
    public boolean isTokenValid(String token) {
        try {
            return extractClaims(token)
                .getExpiration()
                .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Extract all data from token
    private Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}