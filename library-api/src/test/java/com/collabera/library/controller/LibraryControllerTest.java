package com.collabera.library.controller;

import com.collabera.library.entity.BorrowerDetails;
import com.collabera.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryControllerTest {

    private LibraryService libraryService;
    private LibraryController libraryController;

    @BeforeEach
    void setup() {
        libraryService = Mockito.mock(LibraryService.class);
        libraryController = new LibraryController(libraryService);
    }

    @Test
    void registerBorrower_returnsOk() {
        BorrowerDetails input = new BorrowerDetails();
        input.setName("Elon Musk");
        input.setEmail("Elonmusk@gmail.com");

        BorrowerDetails saved = new BorrowerDetails();
        saved.setMemberId(1L);
        saved.setName("Elon Musk");
        saved.setEmail("Elonmusk@gmail.com");

        when(libraryService.registerBorrower(any(BorrowerDetails.class))).thenReturn(saved);

        ResponseEntity<BorrowerDetails> response = libraryController.registerBorrower(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(saved.getMemberId(), response.getBody().getMemberId());
        assertEquals(saved.getName(), response.getBody().getName());

        verify(libraryService, times(1)).registerBorrower(any(BorrowerDetails.class));
    }

    // You can add more tests here for other endpoints in the controller...

}