package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.CreateRatingRequest;
import com.uniquindio.backend.dto.RatingDTO;
import com.uniquindio.backend.model.Book;
import com.uniquindio.backend.model.Rating;
import com.uniquindio.backend.repository.BookRepository;
import com.uniquindio.backend.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;

    public RatingDTO rateBook(CreateRatingRequest request) {
        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + request.getBookId()));

        Rating rating = Rating.builder()
                .book(book)
                .score(request.getScore())
                .build();

        return toDTO(ratingRepository.save(rating));
    }

    public RatingDTO getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));
        return toDTO(rating);
    }

    @Transactional(readOnly = true)
    public List<RatingDTO> getRatingsByBook(Long bookId) {
        return ratingRepository.findByBookIdWithBook(bookId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Double getAverageRating(Long bookId) {
        Double average = ratingRepository.findAverageScoreByBookId(bookId);
        return average != null ? Math.round(average * 10.0) / 10.0 : null;
    }

    public void deleteRating(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new RuntimeException("Rating not found with id: " + id);
        }
        ratingRepository.deleteById(id);
    }

    private RatingDTO toDTO(Rating rating) {
        return RatingDTO.builder()
                .id(rating.getId())
                .bookId(rating.getBook().getId())
                .bookTitle(rating.getBook().getTitle())
                .score(rating.getScore())
                .createdAt(rating.getCreatedAt())
                .updatedAt(rating.getUpdatedAt())
                .build();
    }
}