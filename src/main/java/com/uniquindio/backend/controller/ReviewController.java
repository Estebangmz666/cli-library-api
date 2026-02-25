package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.CreateReviewRequest;
import com.uniquindio.backend.dto.ReviewDTO;
import com.uniquindio.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    @GetMapping("/book/{bookId}/published")
    public ResponseEntity<List<ReviewDTO>> getPublishedReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getPublishedReviewsByBook(bookId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id,
                                                   @RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ReviewDTO> publishReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.publishReview(id));
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<ReviewDTO> archiveReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.archiveReview(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
