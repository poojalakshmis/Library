package com.collabera.library.service;

import com.collabera.library.entity.BookDetails;
import com.collabera.library.entity.BorrowerDetails;
import com.collabera.library.entity.BorrowingRecord;
import com.collabera.library.exception.BadRequestException;
import com.collabera.library.exception.ResourceNotFoundException;
import com.collabera.library.repository.BookDetailsRepository;
import com.collabera.library.repository.BorrowerDetailsRepository;
import com.collabera.library.repository.BorrowingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private BorrowerDetailsRepository borrowerRepo;

    @Mock
    private BookDetailsRepository bookRepo;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_shouldThrowResourceNotFoundException_whenBorrowerNotFound() {
        // borrowerRepo returns empty optional simulating borrower not found
        when(borrowerRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> libraryService.borrowBook(1L, 1L));

        assertEquals("Borrower not found.", exception.getMessage());
    }

    @Test
    void borrowBook_shouldThrowResourceNotFoundException_whenBookNotFound() {
        BorrowerDetails borrower = new BorrowerDetails();
        borrower.setMemberId(1L);
        when(borrowerRepo.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> libraryService.borrowBook(1L, 1L));

        assertEquals("Book not found.", exception.getMessage());
    }

    @Test
    void borrowBook_shouldThrowBadRequestException_whenBookAlreadyBorrowed() {
        BorrowerDetails borrower = new BorrowerDetails();
        borrower.setMemberId(1L);
        BookDetails book = new BookDetails();
        book.setId(1L);
        book.setBorrowed(true); // Book already borrowed

        when(borrowerRepo.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> libraryService.borrowBook(1L, 1L));

        assertEquals("Book is already borrowed.", exception.getMessage());
    }

    @Test
    void borrowBook_shouldSucceed_whenBookAvailableAndBorrowerExists() {
        BorrowerDetails borrower = new BorrowerDetails();
        borrower.setMemberId(1L);

        BookDetails book = new BookDetails();
        book.setId(1L);
        book.setBorrowed(false); // Book is available

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setBorrower(borrower);

        when(borrowerRepo.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRecordRepo.save(any(BorrowingRecord.class))).thenReturn(record);

        BorrowingRecord result = libraryService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(borrower, result.getBorrower());
        assertEquals(book, result.getBook());
        verify(borrowingRecordRepo).save(any(BorrowingRecord.class));
    }
}
