package com.readlog.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readlog.backend.dto.AuthResponse;
import com.readlog.backend.dto.RegisterRequest;
import com.readlog.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

// @RestController = this class handles HTTP requests
@RestController
// @RequestMapping = all endpoints in this class start with /api/auth
@RequestMapping("/api/auth")
// Allow requests from your frontend (GitHub Pages)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/register
    // Frontend sends: { name, email, password }
    // We send back: { token, name, email, message }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Send error message back to frontend
            return ResponseEntity
                .badRequest()
                .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    // POST /api/auth/login
    // Frontend sends: { email, password }
    // We send back: { token, name, email, message }
    @PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> request) {
    try {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, null, null, "Email is required!"));
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, null, null, "Password is required!"));
        }

        AuthResponse response = authService.login(email, password);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    // GET /api/auth/test — just to check if backend is running
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ReadLog backend is running!");
    }
}