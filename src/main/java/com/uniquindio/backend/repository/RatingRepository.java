package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByBookId(Long bookId);

    @Query("SELECT r FROM Rating r JOIN FETCH r.book WHERE r.book.id = :bookId")
    List<Rating> findByBookIdWithBook(@Param("bookId") Long bookId);

    long countByBookId(Long bookId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.book.id = :bookId")
    Double findAverageScoreByBookId(@Param("bookId") Long bookId);
}
