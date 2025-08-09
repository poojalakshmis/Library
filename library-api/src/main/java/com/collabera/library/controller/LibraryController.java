package com.collabera.library.controller;

import com.collabera.library.entity.BorrowingRecord;
import com.collabera.library.service.LibraryService;
import com.collabera.library.entity.BookDetails;
import com.collabera.library.entity.BorrowerDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // Register borrower
    @PostMapping("/borrowers")
    public ResponseEntity<BorrowerDetails> registerBorrower(@Valid @RequestBody BorrowerDetails borrowerDetail) {
        BorrowerDetails saved = libraryService.registerBorrower(borrowerDetail);
        return ResponseEntity.ok(saved);
    }

    // Register book
    @PostMapping("/books")
    public ResponseEntity<BookDetails> registerBook(@Valid @RequestBody BookDetails bookDetails) {
        BookDetails saved = libraryService.registerBook(bookDetails);
        return ResponseEntity.ok(saved);
    }

    // Get all books
    @GetMapping("/books")
    public ResponseEntity<List<BookDetails>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    // Borrow book
    @PostMapping("/borrowers/{memberId}/borrow/{bookId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        return ResponseEntity.ok(libraryService.borrowBook(memberId, bookId));
    }

    // Return book
    @PostMapping("/borrowers/{memberId}/return/{bookId}")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        return ResponseEntity.ok(libraryService.returnBook(memberId, bookId));
    }
}
