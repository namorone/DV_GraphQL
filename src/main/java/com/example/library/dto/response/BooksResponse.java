package com.example.library.dto.response;

import com.example.library.model.Book;
import java.util.List;

public record BooksResponse(Boolean success, String message, List<Book> data) implements ResponseType { }