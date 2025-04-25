package com.example.graphqlclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    public HttpGraphQlClient graphQlClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://countries.trevorblades.com")
                .build();
        return HttpGraphQlClient.builder(webClient).build();
    }
}