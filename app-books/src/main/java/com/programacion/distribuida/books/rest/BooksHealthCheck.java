package com.programacion.distribuida.books.rest;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BooksHealthCheck implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up().withDetail("check", "Simple health check").build();
    }
}