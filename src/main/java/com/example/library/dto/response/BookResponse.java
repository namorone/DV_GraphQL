package com.example.library.dto.response;

import com.example.library.model.Book;

public record BookResponse(Boolean success, String message, Book data) implements ResponseType { }