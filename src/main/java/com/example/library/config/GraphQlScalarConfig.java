package com.example.library.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlScalarConfig {
    @Bean
    RuntimeWiringConfigurer scalarConfigurer() {
        return builder -> builder.scalar(ExtendedScalars.Date);
    }
}