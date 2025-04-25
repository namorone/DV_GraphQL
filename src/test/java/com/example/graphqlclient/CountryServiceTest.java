package com.example.graphqlclient;

import com.example.graphqlclient.model.Country;
import com.example.graphqlclient.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CountryServiceTest {

    @Autowired
    CountryService service;

    @Test
    void shouldReturnUkraine() {
        Country ua = service.country("UA").block();
        assertEquals("Ukraine", ua.name());
        assertEquals("Kyiv", ua.capital());
    }
}

