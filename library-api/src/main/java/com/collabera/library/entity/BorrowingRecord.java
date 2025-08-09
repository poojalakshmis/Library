package com.collabera.library.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BorrowerDetails borrower;

    @OneToOne
    private BookDetails book;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BorrowerDetails getBorrower() {
        return borrower;
    }

    public void setBorrower(BorrowerDetails borrower) {
        this.borrower = borrower;
    }

    public BookDetails getBook() {
        return book;
    }

    public void setBook(BookDetails book) {
        this.book = book;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDateTime borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
