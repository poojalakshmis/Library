package com.collabera.library.repository;
import com.collabera.library.entity.BorrowingRecord;
import com.collabera.library.entity.BookDetails;
import com.collabera.library.entity.BorrowerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookAndReturnedAtIsNull(BookDetails book);

    Optional<BorrowingRecord> findByBorrowerAndBookAndReturnedAtIsNull(BorrowerDetails borrower, BookDetails book);
}
