package org.example.advancedlibrarycatalog.controller;

import jakarta.validation.Valid;
import org.example.advancedlibrarycatalog.model.Book;
import org.example.advancedlibrarycatalog.service.BookService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // POST: Add book (201)
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book saved = service.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET: Smart search (200)
    // Example: /api/v1/books?author=John&sort=desc&page=1
    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<Book> result = service.findBooks(author, sort, page, 10);
        return ResponseEntity.ok(result);
    }

    // GET single book with HATEOAS links (200 or 404)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable Long id) {
        return service.findById(id)
                .map(book -> {
                    EntityModel<Book> model = EntityModel.of(book);

                    // self link
                    model.add(linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel());

                    // link to all books (default params)
                    model.add(linkTo(methodOn(BookController.class).getBooks(null, "asc", 0))
                            .withRel("all-books"));

                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT update (200 or 404)
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return service.update(id, book)
                .map(updated -> ResponseEntity.ok(updated))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE (204 if deleted, 404 if not found) - protected by SecurityConfig (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
    }
}
