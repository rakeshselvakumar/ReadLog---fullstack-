package com.readlog.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readlog.backend.dto.BookRequest;
import com.readlog.backend.dto.BookResponse;
import com.readlog.backend.entity.Book;
import com.readlog.backend.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET all books — returns list without password
    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<BookResponse> books = bookService
            .getBooks(userDetails.getUsername())
            .stream()
            .map(BookResponse::from)  // convert each Book to BookResponse
            .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // POST — add book
    @PostMapping
    public ResponseEntity<?> addBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookRequest request) {
        try {
            Book book = bookService.addBook(userDetails.getUsername(), request);
            return ResponseEntity.ok(BookResponse.from(book));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // PUT — edit book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody BookRequest request) {
        try {
            Book book = bookService.updateBook(userDetails.getUsername(), id, request);
            return ResponseEntity.ok(BookResponse.from(book));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // DELETE — delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            bookService.deleteBook(userDetails.getUsername(), id);
            return ResponseEntity.ok(Map.of("message", "Book deleted!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}