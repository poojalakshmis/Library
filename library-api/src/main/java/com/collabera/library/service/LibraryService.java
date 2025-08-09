package com.collabera.library.service;

import com.collabera.library.entity.BorrowingRecord;
import com.collabera.library.repository.BorrowerDetailsRepository;
import com.collabera.library.repository.BorrowingRecordRepository;
import com.collabera.library.exception.BadRequestException;
import com.collabera.library.exception.ResourceNotFoundException;
import com.collabera.library.entity.BookDetails;
import com.collabera.library.entity.BorrowerDetails;
import com.collabera.library.repository.BookDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class LibraryService {

    private final BorrowerDetailsRepository borrowerRepo;
    private final BookDetailsRepository bookRepo;
    private final BorrowingRecordRepository borrowingRecordRepo;

    public LibraryService(BorrowerDetailsRepository borrowerRepo, BookDetailsRepository bookRepo,
                          BorrowingRecordRepository borrowingRecordRepo) {
        this.borrowerRepo = borrowerRepo;
        this.bookRepo = bookRepo;
        this.borrowingRecordRepo = borrowingRecordRepo;
    }

    // Register borrower
    public BorrowerDetails registerBorrower(BorrowerDetails borrowerDetail) {
        borrowerRepo.findByEmail(borrowerDetail.getEmail())
                .ifPresent(b -> {throw new BadRequestException("Email already registered.");});
        return borrowerRepo.save(borrowerDetail);
    }

    // Register book
    public BookDetails registerBook(BookDetails bookDetails) {
        bookRepo.findByIsbn(bookDetails.getIsbn())
                .stream()
                .filter(b -> !b.getTitle().equals(bookDetails.getTitle()) || !b.getAuthor().equals(bookDetails.getAuthor()))
                .findAny()
                .ifPresent(b -> {
                    throw new BadRequestException("ISBN must correspond to same title and author.");
                });
        return bookRepo.save(bookDetails);
    }

    // Get all books
    public List<BookDetails> getAllBooks() {
        return bookRepo.findAll();
    }

    // Borrow book
    @Transactional
    public BorrowingRecord borrowBook(Long memberId, Long bookId) {
        BorrowerDetails borrower = borrowerRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found."));
        BookDetails book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        if (book.isBorrowed()) {
            throw new BadRequestException("Book is already borrowed.");
        }

        book.setBorrowed(true);
        bookRepo.save(book);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setBorrower(borrower);
        record.setBorrowedAt(LocalDateTime.now());
        return borrowingRecordRepo.save(record);
    }

    // Return book
    @Transactional
    public BorrowingRecord returnBook(Long memberId, Long bookId) {
        BorrowerDetails borrower = borrowerRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found."));
        BookDetails book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        BorrowingRecord record = borrowingRecordRepo.findByBorrowerAndBookAndReturnedAtIsNull(borrower, book)
                .orElseThrow(() -> new BadRequestException("This borrower did not borrow this book or already returned."));

        record.setReturnedAt(LocalDateTime.now());

        book.setBorrowed(false);
        bookRepo.save(book);

        return borrowingRecordRepo.save(record);
    }
}
