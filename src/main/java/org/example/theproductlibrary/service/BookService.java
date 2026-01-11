package org.example.theproductlibrary.service;

import jakarta.annotation.PreDestroy;
import org.example.theproductlibrary.model.Book;
import org.example.theproductlibrary.model.BookStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BookService {

    // In-memory store (thread-safe for concurrent reads)
    private final List<Book> books = new CopyOnWriteArrayList<>();

    // Phase 2: background processing pool
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public Book addBookAsync(String title) {
        Book book = new Book(
                UUID.randomUUID().toString(),
                title,
                BookStatus.PROCESSING
        );

        books.add(book);

        executor.submit(() -> {
            try {
                Thread.sleep(3000); // simulate barcode printing
                book.setStatus(BookStatus.AVAILABLE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // In a real system you might set status to FAILED, log, etc.
            }
        });

        return book;
    }

    public List<Book> getAll() {
        return books;
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}
