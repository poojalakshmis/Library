package com.collabera.library.repository;

import com.collabera.library.entity.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookDetailsRepository extends JpaRepository<BookDetails, Long> {
    List<BookDetails> findByIsbn(String isbn);

    Optional<BookDetails> findByIsbnAndTitleAndAuthor(String isbn, String title, String author);

}