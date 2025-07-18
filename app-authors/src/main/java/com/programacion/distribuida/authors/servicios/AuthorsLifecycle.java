package com.programacion.distribuida.authors.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthorsLifecycle {

    @Value("${spring.cloud.consul.host:127.0.0.1}")
    private String consulHost;

    @Value("${spring.cloud.consul.port:8500}")
    private Integer consulPort;

    @Value("${server.port:8080}")
    private Integer appPort;

    @Value("${spring.application.name:app-authors}")
    private String applicationName;

    private String serviceId;
    private final RestTemplate restTemplate = new RestTemplate();

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() throws Exception {
        serviceId = applicationName + "-" + UUID.randomUUID();
        String ipAddress = InetAddress.getLocalHost().getHostAddress();

        String consulUrl = String.format("http://%s:%d/v1/agent/service/register", consulHost, consulPort);

        Map<String, Object> check = Map.of(
                "http", String.format("http://%s:%d/actuator/health", ipAddress, appPort),
                "interval", "10s",
                "DeregisterCriticalServiceAfter", "20s"
        );

        Map<String, Object> service = Map.of(
                "Name", applicationName,
                "ID", serviceId,
                "Address", ipAddress,
                "Port", appPort,
                "Tags", List.of(
                        "traefik.enable=true",
                        "traefik.http.routers." + applicationName + ".entrypoints=web",
                        "traefik.http.routers." + applicationName + ".rule=PathPrefix(`/authors`)",
                        "traefik.http.services." + applicationName + ".loadbalancer.server.port=" + appPort,
                        "traefik.http.services." + applicationName + ".loadbalancer.server.scheme=http"
                ),
                "Check", check
        );

        restTemplate.put(consulUrl, service);
        System.out.println("Servicio registrado en Consul: " + serviceId + " en puerto: " + appPort);
    }

    @EventListener(ContextClosedEvent.class)
    public void deregisterService() {
        String consulUrl = String.format("http://%s:%d/v1/agent/service/deregister/%s", consulHost, consulPort, serviceId);
        restTemplate.put(consulUrl, null);
    }
}
