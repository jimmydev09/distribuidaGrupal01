package com.programacion.distribuida.customers.rest;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomersHealthCheck implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up().withDetail("check", "Simple health check").build();
    }
}