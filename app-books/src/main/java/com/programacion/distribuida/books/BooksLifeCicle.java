package com.programacion.distribuida.books;


import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

@Component
public class BooksLifeCicle{

    @Autowired
    private ConsulClient consulClient;

    private String serviceId;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int servicePort;

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception {
        serviceId = UUID.randomUUID().toString();
        String address = InetAddress.getLocalHost().getHostAddress();

        NewService service = new NewService();
        service.setId(serviceId);
        service.setName(serviceName);
        service.setAddress(address);
        service.setPort(servicePort);
        service.setTags(List.of(
                "traefik.enable=true",
                "traefik.http.routers.app-books.rule=PathPrefix(`/books`)",
                "traefik.http.middlewares.strip-prefix-books.stripPrefix.prefixes=/app-books"
        ));

        NewService.Check check = new NewService.Check();
        check.setHttp("http://" + address + ":" + servicePort + "/actuator/health/liveness");
        check.setInterval("10s");
        check.setDeregisterCriticalServiceAfter("20s");
        service.setCheck(check);

        consulClient.agentServiceRegister(service);
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        consulClient.agentServiceDeregister(serviceId);
    }
}
