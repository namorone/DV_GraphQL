package com.example.dgs;

import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Profile;
import com.example.repository.DataRepository;
import com.netflix.graphql.dgs.*;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class AuthorDataFetcher {
    private static final Logger logger = LoggerFactory.getLogger(AuthorDataFetcher.class);
    private final DataRepository dataRepository;

    public AuthorDataFetcher(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // Запит для отримання списку авторів
    @DgsQuery
    public List<Author> authors() {
        return dataRepository.getAuthors();
    }

    // Запит для отримання автора за id
    @DgsQuery
    public Author authorById(@InputArgument String id) {
        return dataRepository.getAuthorById(id);
    }

    // Використання DataLoader для поля profile (об’єкт)
    @DgsData(parentType = "Author", field = "profile")
    public CompletableFuture<Profile> getProfile(DgsDataFetchingEnvironment dfe) {
        Author author = dfe.getSource();
        DataLoader<String, Profile> dataLoader = dfe.getDataLoader("PROFILE");
        return dataLoader.load(author.getId());
    }

    // Використання DataLoader для поля books (список)
    @DgsData(parentType = "Author", field = "books")
    public CompletableFuture<List<Book>> getBooks(DgsDataFetchingEnvironment dfe) {
        Author author = dfe.getSource();
        DataLoader<String, List<Book>> dataLoader = dfe.getDataLoader("BOOKS");
        return dataLoader.load(author.getId());
    }

    // Пряме завантаження книг без DataLoader (для демонстрації N+1 запиту)
    @DgsData(parentType = "Author", field = "booksDirect")
    public List<Book> getBooksDirect(DgsDataFetchingEnvironment dfe) {
        Author author = dfe.getSource();
        logger.info("Пряме завантаження книг для автора id: {}", author.getId());
        return dataRepository.getBooksByAuthorId(author.getId());
    }
}
