package com.example.dataloader;

import com.example.model.Book;
import com.example.repository.DataRepository;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.BatchLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsDataLoader(name = "BOOKS")
@Component
public class BooksDataLoader implements BatchLoader<String, List<Book>> {
    private static final Logger logger = LoggerFactory.getLogger(BooksDataLoader.class);
    private final DataRepository dataRepository;

    public BooksDataLoader(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public CompletableFuture<List<List<Book>>> load(List<String> authorIds) {
        logger.info("Пакетне завантаження книг для авторів: {}", authorIds);
        List<List<Book>> booksList = authorIds.stream()
                .map(id -> dataRepository.getBooksByAuthorId(id))
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(booksList);
    }
}
