package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book find(Long id) { return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book " + id + " not found")); }
    public List<Book> findAll() { return repo.findAll(); }
    public Book save(Book b) { return repo.save(b); }
}