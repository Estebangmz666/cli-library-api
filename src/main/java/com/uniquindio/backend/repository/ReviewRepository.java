package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Review;
import com.uniquindio.backend.model.Review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);

    @Query("SELECT r FROM Review r JOIN FETCH r.book WHERE r.book.id = :bookId")
    List<Review> findByBookIdWithBook(@Param("bookId") Long bookId);

    long countByBookId(Long bookId);

    @Query("SELECT r FROM Review r JOIN FETCH r.book WHERE r.book.id = :bookId AND r.status = :status")
    List<Review> findByBookIdAndStatusWithBook(@Param("bookId") Long bookId, @Param("status") ReviewStatus status);
}