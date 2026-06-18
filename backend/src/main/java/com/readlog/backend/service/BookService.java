package com.readlog.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readlog.backend.dto.BookRequest;
import com.readlog.backend.entity.Book;
import com.readlog.backend.entity.User;
import com.readlog.backend.repository.BookRepository;
import com.readlog.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // GET all books for logged in user
    public List<Book> getBooks(String email) {
        User user = getUser(email);
        return bookRepository.findByUserOrderByDateAddedDesc(user);
    }

    // ADD a new book
    public Book addBook(String email, BookRequest request) {
        User user = getUser(email);

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Book title is required!");
        }

        Book book = new Book();
        book.setUser(user);
        mapRequestToBook(request, book);

        return bookRepository.save(book);
    }

    // EDIT an existing book
    public Book updateBook(String email, Long bookId, BookRequest request) {
        User user = getUser(email);

        // findByIdAndUser ensures user can only edit THEIR books
        Book book = bookRepository.findByIdAndUser(bookId, user)
            .orElseThrow(() -> new RuntimeException("Book not found!"));

        mapRequestToBook(request, book);
        return bookRepository.save(book);
    }

    // DELETE a book
    public void deleteBook(String email, Long bookId) {
        User user = getUser(email);

        Book book = bookRepository.findByIdAndUser(bookId, user)
            .orElseThrow(() -> new RuntimeException("Book not found!"));

        bookRepository.delete(book);
    }

    // Helper — find user by email
    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    // Helper — copy request data into book object
    private void mapRequestToBook(BookRequest request, Book book) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setStatus(request.getStatus() != null ? request.getStatus() : "want");
        book.setRating(request.getRating());
        book.setNotes(request.getNotes());
        book.setCover(request.getCover());
        book.setCurrentPage(request.getCurrentPage());
        book.setTotalPages(request.getTotalPages());

        // Convert "2024-06" string to LocalDateTime
        if (request.getDateRead() != null && !request.getDateRead().isBlank()) {
            try {
                String[] parts = request.getDateRead().split("-");
                int year  = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                book.setDateRead(LocalDateTime.of(year, month, 1, 0, 0));
            } catch (Exception e) {
                book.setDateRead(null);
            }
        } else {
            book.setDateRead(null);
        }
    }
}