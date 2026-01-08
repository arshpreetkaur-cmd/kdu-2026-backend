package org.example.restfundamentals.service;
import org.example.restfundamentals.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {
    private final Map<Long,Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    //creating a book
    public Book addBook(Book book){
        Long id = idGenerator.getAndIncrement();
        Book savedBook = new Book(id, book.getTitle(), book.getAuthor());
        books.put(id, savedBook);
        return savedBook;
    }

    //reading a book
    public List<Book> getAllBooks(){
        return new ArrayList<>(books.values());
    }

    //updating a book
    public Optional<Book> updateBook(Long id, Book book){
        if(!books.containsKey(id)){
            return Optional.empty();
        }
        Book updated = new Book(id, book.getTitle(), book.getAuthor());
        books.put(id, updated);
        return Optional.of(updated);
    }

    //deleting a book
    public boolean deleteBook(Long id){
        return books.remove(id) != null;
    }
}
