package com.programacion.distribuida.customers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomerLifecycle {

    @Value("${consul.host:127.0.0.1}")
    private String consulHost;

    @Value("${consul.port:8500}")
    private Integer consulPort;

    @Value("${server.port:8080}")
    private Integer appPort;

    private String serviceId;

    private final RestTemplate restTemplate = new RestTemplate();

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws Exception {
        System.out.println("Starting Customers service...");

        serviceId = UUID.randomUUID().toString();
        String ipAddress = InetAddress.getLocalHost().getHostAddress();

        String consulUrl = String.format("http://%s:%d/v1/agent/service/register", consulHost, consulPort);

        Map<String, Object> check = Map.of(
                "http", String.format("http://%s:%d/actuator/health", ipAddress, appPort),
                "interval", "10s",
                "DeregisterCriticalServiceAfter", "20s"
        );

        Map<String, Object> service = Map.of(
                "Name", "app-customers",
                "ID", serviceId,
                "Address", ipAddress,
                "Port", appPort,
                "Tags", List.of(
                        "traefik.enable=true",
                        "traefik.http.routers.app-customers.rule=PathPrefix(`/customers`)",
                        "traefik.http.routers.app-customers.middlewares=strip-prefix-customers",
                        "traefik.http.middlewares.strip-prefix-customers.stripPrefix.prefixes=/app-customers"
                ),
                "Check", check
        );

        restTemplate.put(consulUrl, service);
    }

    @EventListener
    public void onApplicationShutdown(ContextClosedEvent event) {
        System.out.println("Stopping Customers service...");

        String consulUrl = String.format("http://%s:%d/v1/agent/service/deregister/%s", consulHost, consulPort, serviceId);
        restTemplate.put(consulUrl, null);
    }
}