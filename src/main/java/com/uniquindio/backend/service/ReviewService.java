package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.CreateReviewRequest;
import com.uniquindio.backend.dto.ReviewDTO;
import com.uniquindio.backend.model.Book;
import com.uniquindio.backend.model.Review;
import com.uniquindio.backend.model.Review.ReviewStatus;
import com.uniquindio.backend.repository.BookRepository;
import com.uniquindio.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewDTO createReview(CreateReviewRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + request.getBookId()));

        Review review = Review.builder()
                .book(book)
                .title(request.getTitle())
                .content(request.getContent())
                .status(ReviewStatus.DRAFT)
                .build();

        return toDTO(reviewRepository.save(review));
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        return toDTO(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookIdWithBook(bookId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getPublishedReviewsByBook(Long bookId) {
        return reviewRepository.findByBookIdAndStatusWithBook(bookId, ReviewStatus.PUBLISHED).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO publishReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        review.setStatus(ReviewStatus.PUBLISHED);
        return toDTO(reviewRepository.save(review));
    }

    public ReviewDTO archiveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        review.setStatus(ReviewStatus.ARCHIVED);
        return toDTO(reviewRepository.save(review));
    }

    public ReviewDTO updateReview(Long id, CreateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        if (request.getTitle() != null) review.setTitle(request.getTitle());
        if (request.getContent() != null) review.setContent(request.getContent());

        return toDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .bookId(review.getBook().getId())
                .bookTitle(review.getBook().getTitle())
                .title(review.getTitle())
                .content(review.getContent())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
