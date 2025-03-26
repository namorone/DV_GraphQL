package com.example.repository;

import com.example.model.Author;
import com.example.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataRepository {
    private List<Author> authors;
    private List<Book> books;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream authorStream = getClass().getResourceAsStream("/data/authors.json");
            authors = mapper.readValue(authorStream, new TypeReference<List<Author>>(){});

            InputStream bookStream = getClass().getResourceAsStream("/data/books.json");
            books = mapper.readValue(bookStream, new TypeReference<List<Book>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Помилка завантаження даних", e);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Author getAuthorById(String id) {
        return authors.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Book> getBooksByAuthorId(String authorId) {
        return books.stream().filter(b -> b.getAuthorId().equals(authorId)).collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return books;
    }
}
