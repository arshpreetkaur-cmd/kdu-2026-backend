package org.example.theproductlibrary.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.theproductlibrary.model.Book;
import org.example.theproductlibrary.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Small DTO so we don't expose internal model constraints in request body
    public static class CreateBookRequest {
        public String title;
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Starts the async book processing")
    public ResponseEntity<Book> create(@RequestBody CreateBookRequest req) {
        Book created = bookService.addBookAsync(req.title);
        // Phase 2 requirement: return immediately with 202 Accepted
        return ResponseEntity.accepted().body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN','MEMBER')")
    public ResponseEntity<List<Book>> list() {
        return ResponseEntity.ok(bookService.getAll());
    }
}