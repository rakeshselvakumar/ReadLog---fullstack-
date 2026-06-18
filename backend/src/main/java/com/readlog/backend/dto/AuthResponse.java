package com.readlog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// This is what we send BACK to the frontend after login/register
// Contains the JWT token and basic user info
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;      // JWT token
    private String name;       // User's name to show in UI
    private String email;      // User's email
    private String message;    // Success/error message
}