package org.example.theproductlibrary.controller;

import org.example.theproductlibrary.model.BookStatus;
import org.example.theproductlibrary.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final BookService bookService;

    public AnalyticsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/audit")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MEMBER')")
    public Map<String, Long> audit() {
        // Phase 3: groupingBy + counting
        Map<BookStatus, Long> grouped = bookService.getAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getStatus(),
                        Collectors.counting()
                ));

        // Convert enum keys to String keys for the exact expected JSON shape
        return grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }
}