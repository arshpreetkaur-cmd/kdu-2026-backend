package org.example.advancedlibrarycatalog.service;

import org.example.advancedlibrarycatalog.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final Map<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // CREATE
    public Book create(Book incoming) {
        Long id = idGenerator.getAndIncrement();
        Book saved = new Book(id, incoming.getTitle(), incoming.getAuthor());
        store.put(id, saved);
        return saved;
    }

    // READ (all) - used by smart search
    public List<Book> findBooks(String author, String sortDir, int page, int size) {
        List<Book> books = new ArrayList<>(store.values());

        // Filter by author if provided
        if (author != null && !author.isBlank()) {
            books = books.stream()
                    .filter(b -> b.getAuthor() != null && b.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
        }

        // Sort by title (asc default, desc optional)
        if ("desc".equalsIgnoreCase(sortDir)) {
            books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
        } else {
            books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        }

        // Pagination
        int start = page * size;
        if (start >= books.size()) return List.of();

        int end = Math.min(start + size, books.size());
        return books.subList(start, end);
    }

    // READ (single)
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    // UPDATE
    public Optional<Book> update(Long id, Book incoming) {
        Book existing = store.get(id);
        if (existing == null) return Optional.empty();

        Book updated = new Book(id, incoming.getTitle(), incoming.getAuthor());
        store.put(id, updated);
        return Optional.of(updated);
    }

    // DELETE
    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
