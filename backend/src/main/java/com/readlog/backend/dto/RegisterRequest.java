package com.readlog.backend.dto;

import lombok.Data;

// DTO = Data Transfer Object
// This holds the data coming FROM the frontend registration form
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}