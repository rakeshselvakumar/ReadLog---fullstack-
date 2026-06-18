package com.readlog.backend.service;

import com.readlog.backend.dto.AuthResponse;
import com.readlog.backend.dto.RegisterRequest;
import com.readlog.backend.entity.User;
import com.readlog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // Lombok auto-creates constructor with all fields
public class AuthService {

    // Spring automatically injects these
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // REGISTER — create a new user account
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists in database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Validate fields are not empty
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Name is required!");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters!");
        }

        // Create new user object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase().trim());

        // NEVER save plain text password
        // passwordEncoder.encode() turns "mypassword" into "$2a$10$xyz..."
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save user to MySQL database
        userRepository.save(user);

        // Generate JWT token for the new user
        String token = jwtService.generateToken(user.getEmail());

        // Return token + user info to frontend
        return new AuthResponse(
            token,
            user.getName(),
            user.getEmail(),
            "Account created successfully!"
        );
    }

    // LOGIN — check credentials and return token
    public AuthResponse login(String email, String password) {

        // Find user by email
        User user = userRepository.findByEmail(email.toLowerCase().trim())
            .orElseThrow(() -> new RuntimeException("No account found with this email!"));

        // Check if password matches the stored encrypted password
        // passwordEncoder.matches() compares plain text with encrypted
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
            token,
            user.getName(),
            user.getEmail(),
            "Login successful!"
        );
    }
}