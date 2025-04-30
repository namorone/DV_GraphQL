package com.example.library.service;

import com.example.library.dto.Author;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class AuthorService {
    private final RestClient rest = RestClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();

    public Author getAuthor(Long id) {
        try {
            ApiAuthor api = rest.get().uri("/users/{id}", id).retrieve().body(ApiAuthor.class);
            if (api == null) throw new IllegalStateException("Author API returned null");
            return new Author(api.id(), api.name(), api.company().catchPhrase());
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Author " + id + " not found");
        }
    }

    /* nested DTOs mapping JSONPlaceholder */
    private record ApiAuthor(Long id, String name, ApiCompany company) { }
    private record ApiCompany(String catchPhrase) { }
}