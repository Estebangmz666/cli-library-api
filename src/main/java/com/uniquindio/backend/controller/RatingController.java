package com.uniquindio.backend.controller;

import com.uniquindio.backend.dto.CreateRatingRequest;
import com.uniquindio.backend.dto.RatingDTO;
import com.uniquindio.backend.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingDTO> rateBook(@RequestBody CreateRatingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rateBook(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RatingDTO>> getRatingsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(ratingService.getRatingsByBook(bookId));
    }

    @GetMapping("/book/{bookId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long bookId) {
        return ResponseEntity.ok(ratingService.getAverageRating(bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
