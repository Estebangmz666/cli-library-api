package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.BookDTO;
import com.uniquindio.backend.dto.CreateBookRequest;
import com.uniquindio.backend.dto.UpdateBookRequest;
import com.uniquindio.backend.model.Book;
import com.uniquindio.backend.repository.BookRepository;
import com.uniquindio.backend.repository.RatingRepository;
import com.uniquindio.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;

    public BookDTO createBook(CreateBookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .coverImage(request.getCoverImage())
                .genre(request.getGenre())
                .publishedYear(request.getPublishedYear())
                .price(request.getPrice())
                .build();

        return toDTO(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return toDTO(book);
    }

    public BookDTO updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getDescription() != null) book.setDescription(request.getDescription());
        if (request.getCoverImage() != null) book.setCoverImage(request.getCoverImage());
        if (request.getGenre() != null) book.setGenre(request.getGenre());
        if (request.getPublishedYear() != null) book.setPublishedYear(request.getPublishedYear());
        if (request.getPrice() != null) book.setPrice(request.getPrice());

        return toDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchBooks(String title, String author, String genre) {
        return bookRepository.search(title, author, genre).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksAdvanced(String title, String author, String isbn) {
        return bookRepository.searchByTitleAuthorIsbn(title, author, isbn).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksByText(String term) {
        return bookRepository.searchByTitleOrAuthor(term).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BookDTO toDTO(Book book) {
        Double average = ratingRepository.findAverageScoreByBookId(book.getId());
        long totalRatings = ratingRepository.countByBookId(book.getId());
        long totalReviews = reviewRepository.countByBookId(book.getId());
        int totalRatingsInt = totalRatings > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalRatings;
        int totalReviewsInt = totalReviews > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalReviews;
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .genre(book.getGenre())
                .publishedYear(book.getPublishedYear())
                .price(book.getPrice())
                .averageRating(average != null ? Math.round(average * 10.0) / 10.0 : null)
                .totalRatings(totalRatingsInt)
                .totalReviews(totalReviewsInt)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}