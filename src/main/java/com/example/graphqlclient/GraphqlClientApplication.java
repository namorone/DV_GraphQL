package com.example.graphqlclient;

import com.example.graphqlclient.service.CountryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphqlClientApplication implements CommandLineRunner {

    private final CountryService countryService;

    public GraphqlClientApplication(CountryService countryService) {
        this.countryService = countryService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GraphqlClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // приклад 1: отримати 5 країн Європи
        countryService.countriesByContinent("EU")
                .doOnNext(System.out::println)
                .blockLast();

        // приклад 2: одну країну за кодом
        countryService.country("UA")
                .doOnNext(c -> System.out.printf("Capital of %s is %s\n", c.name(), c.capital()))
                .block();
    }
}