package com.example.graphqlclient.service;

import com.example.graphqlclient.model.Country;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CountryService {

    private final HttpGraphQlClient client;

    public CountryService(HttpGraphQlClient client) {
        this.client = client;
    }

    public Flux<Country> countriesByContinent(String code) {
        String document = """
            query($code: ID!) {
                continent(code: $code) {
                    countries {
                        code name capital emoji
                    }
                }
            }
            """;
        return client.document(document)
                .variable("code", code)
                .retrieve("continent.countries")
                .toEntityList(Country.class)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Country> country(String code) {
        String document = """
            query($code: ID!) {
                country(code: $code) { code name capital emoji }
            }
            """;
        return client.document(document)
                .variable("code", code)
                .retrieve("country")
                .toEntity(Country.class);
    }
}
