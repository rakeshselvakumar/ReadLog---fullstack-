package com.readlog.backend.dto;

import java.time.LocalDateTime;

import com.readlog.backend.entity.Book;

import lombok.Data;

// This is what we send to frontend
// Notice — NO password field!
@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String status;
    private Integer rating;
    private String notes;
    private String cover;
    private Integer currentPage;
    private Integer totalPages;
    private LocalDateTime dateRead;
    private LocalDateTime dateAdded;

    // Convert Book entity to BookResponse
    public static BookResponse from(Book book) {
        BookResponse res = new BookResponse();
        res.setId(book.getId());
        res.setTitle(book.getTitle());
        res.setAuthor(book.getAuthor());
        res.setGenre(book.getGenre());
        res.setStatus(book.getStatus());
        res.setRating(book.getRating());
        res.setNotes(book.getNotes());
        res.setCover(book.getCover());
        res.setCurrentPage(book.getCurrentPage());
        res.setTotalPages(book.getTotalPages());
        res.setDateRead(book.getDateRead());
        res.setDateAdded(book.getDateAdded());
        return res;
    }
}