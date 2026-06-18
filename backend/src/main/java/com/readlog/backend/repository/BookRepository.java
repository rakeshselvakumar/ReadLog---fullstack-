package com.readlog.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readlog.backend.entity.Book;
import com.readlog.backend.entity.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Get ALL books for a specific user
    // SQL: SELECT * FROM books WHERE user_id = ?
    List<Book> findByUserOrderByDateAddedDesc(User user);

    // Get books by status for a user
    List<Book> findByUserAndStatus(User user, String status);

    // Find specific book by id AND user (security check)
    // Prevents user A from editing user B's books
    Optional<Book> findByIdAndUser(Long id, User user);
}