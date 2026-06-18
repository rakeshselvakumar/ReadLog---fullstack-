package com.readlog.backend.dto;

import lombok.Data;

// Data coming FROM frontend when adding/editing a book
@Data
public class BookRequest {
    private String title;
    private String author;
    private String genre;
    private String status;
    private Integer rating;
    private String notes;
    private String cover;
    private Integer currentPage;
    private Integer totalPages;
    private String dateRead; // "2024-06" format from frontend
}