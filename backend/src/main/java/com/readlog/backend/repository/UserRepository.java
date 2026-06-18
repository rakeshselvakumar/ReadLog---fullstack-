package com.readlog.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readlog.backend.entity.User;

// JpaRepository gives us free methods:
// save(), findById(), findAll(), delete() etc.
// We just add our own custom ones below
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data auto-generates SQL for this:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Check if email already registered
    boolean existsByEmail(String email);
}