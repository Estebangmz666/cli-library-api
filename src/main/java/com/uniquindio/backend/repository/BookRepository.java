package com.uniquindio.backend.repository;

import com.uniquindio.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByGenre(String genre);

    @Query(value = "SELECT * FROM books b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:genre IS NULL OR LOWER(CAST(b.genre AS TEXT)) LIKE LOWER(CONCAT('%', :genre, '%')))",
           nativeQuery = true)
    List<Book> search(@Param("title") String title,
                      @Param("author") String author,
                      @Param("genre") String genre);

    @Query(value = "SELECT * FROM books b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:isbn IS NULL OR LOWER(CAST(b.isbn AS TEXT)) LIKE LOWER(CONCAT('%', :isbn, '%')))",
           nativeQuery = true)
    List<Book> searchByTitleAuthorIsbn(@Param("title") String title,
                                       @Param("author") String author,
                                       @Param("isbn") String isbn);

    @Query(value = "SELECT * FROM books b WHERE " +
           "(:term IS NULL OR " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :term, '%')))",
           nativeQuery = true)
    List<Book> searchByTitleOrAuthor(@Param("term") String term);
}
