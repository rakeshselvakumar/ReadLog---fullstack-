package com.readlog.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each book belongs to a user
    // ManyToOne = many books can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    private String author;
    private String genre;

    // reading, read, want, favourite
    @Column(nullable = false)
    private String status;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Store cover as URL or base64
    @Column(columnDefinition = "LONGTEXT")
    private String cover;

    private Integer currentPage;
    private Integer totalPages;

    // When they actually read the book
    private LocalDateTime dateRead;

    // When they added to app
    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @PrePersist
    public void prePersist() {
        this.dateAdded = LocalDateTime.now();
    }
}