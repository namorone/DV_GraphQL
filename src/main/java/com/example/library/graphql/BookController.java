package com.example.library.graphql;

import com.example.library.dto.response.*;
import com.example.library.model.Book;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class BookController {
    private final BookService books;
    private final AuthorService authors;

    public BookController(BookService books, AuthorService authors) {
        this.books = books;
        this.authors = authors;
    }

    // ------------------ Queries ------------------------------------------------
    @QueryMapping
    public BookResponse book(@Argument Long id) {
        Book b = books.find(id);
        return new BookResponse(true, null, b);
    }

    @QueryMapping
    public BooksResponse books() {
        return new BooksResponse(true, null, books.findAll());
    }

    // ------------------ Mutations ---------------------------------------------
    @MutationMapping
    @PreAuthorize("hasRole('admin')")
    public BookResponse addBook(@Argument AddBookInput input) {
        Book saved = books.save(map(input));
        return new BookResponse(true, "Created", saved);
    }

    // ------------------ Field resolver ----------------------------------------
    @SchemaMapping(typeName = "Book", field = "author")
    public AuthorResponse author(Book book) {
        return new AuthorResponse(true, null, authors.getAuthor(book.getAuthorId()));
    }

    // ------------------ Helper -------------------------------------------------
    private Book map(AddBookInput in) {
        Book b = new Book();
        b.setTitle(in.title());
        b.setPublishedDate(in.publishedDate());
        b.setAuthorId(Long.parseLong(in.authorId()));
        return b;
    }

    // ------------------ Input record ------------------------------------------
    public record AddBookInput(
            @NotBlank String title,
            @NotNull LocalDate publishedDate,
            @NotBlank String authorId) { }
}