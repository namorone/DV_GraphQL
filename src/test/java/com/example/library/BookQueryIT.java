package com.example.library;

import com.example.library.config.GraphQlScalarConfig;
import com.example.library.graphql.BookController;
import com.example.library.repository.BookRepository;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@GraphQlTest(BookController.class)
@Import({BookService.class, AuthorService.class, GraphQlScalarConfig.class})
@ActiveProfiles("test")
class BookQueryIT {
    @Autowired
    GraphQlTester tester;

    @MockBean
    BookRepository repo;

    static MockWebServer mockServer;

    @BeforeAll
    static void startMock() throws IOException {
        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse().setBody("{" +
                "\"id\":1,\"name\":\"Leanne Graham\",\"company\":{\"catchPhrase\":\"Author bio\"}}"));
        mockServer.start(1080);
    }

    @AfterAll
    static void shutdown() throws IOException { mockServer.shutdown(); }

    @Test
    void booksInitiallyEmpty() {
        String doc = "query { books { data { id } } }";
        tester.document(doc)
                .execute()
                .path("books.data")
                .entityList(Object.class)
                .hasSize(0);
    }
}