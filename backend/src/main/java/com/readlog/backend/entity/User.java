package com.readlog.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Entity tells Spring Boot this class = a database table
@Entity
// @Table sets the actual table name in MySQL
@Table(name = "users")
// @Data from Lombok auto-generates getters, setters, toString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // @Id = this is the primary key
    @Id
    // Auto increment — MySQL gives each user a unique number
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column defines the column properties
    @Column(nullable = false)  // name cannot be empty
    private String name;

    // unique = true means no two users can have same email
    @Column(nullable = false, unique = true)
    private String email;

    // We'll store the encrypted password here — never plain text
    @Column(nullable = false)
    private String password;

    // Automatically set when user registers
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // @PrePersist runs automatically before saving to database
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}