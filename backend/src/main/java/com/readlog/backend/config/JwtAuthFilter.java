package com.readlog.backend.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.readlog.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // Runs on every single request to check the JWT token
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Get the Authorization header
        // It looks like: "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // If no token — skip (public endpoints like /api/auth/** are fine)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        try {
            // Extract email from token
            String email = jwtService.extractEmail(token);

            // If token is valid and user not already authenticated
            if (email != null &&
                jwtService.isTokenValid(token) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                // Create authentication object with the email
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        // We create a simple UserDetails with just the email
                        new org.springframework.security.core.userdetails.User(
                            email, "", new ArrayList<>()),
                        null,
                        new ArrayList<>()
                    );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

                // Tell Spring Security this user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Invalid token — just continue without authentication
        }

        filterChain.doFilter(request, response);
    }
}