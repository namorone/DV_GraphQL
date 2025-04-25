package com.example.graphqlclient.controller;

import com.example.graphqlclient.model.Country;
import com.example.graphqlclient.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProxyController {

    private final CountryService service;

    /** GET /api/country/UA */
    @GetMapping("/country/{code}")
    public Mono<Country> country(@PathVariable String code) {
        return service.country(code);
    }

    /** GET /api/continent/EU?limit=3 */
    @GetMapping("/continent/{code}")
    public Flux<Country> continent(@PathVariable String code,
                                   @RequestParam(defaultValue = "5") int limit) {
        return service.countriesByContinent(code).take(limit);
    }
}
