package org.example.theproductlibrary.model;

//this is in-memory domain model
public class Book {
    private String id;
    private String title;
    private BookStatus status;

    public Book(){}

    public Book(String id, String title, BookStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
}
