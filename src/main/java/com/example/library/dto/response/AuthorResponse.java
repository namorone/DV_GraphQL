package com.example.library.dto.response;

import com.example.library.dto.Author;

public record AuthorResponse(Boolean success, String message, Author data) implements ResponseType { }