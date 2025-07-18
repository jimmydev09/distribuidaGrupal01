package com.programacion.distribuida.customers.rest;

import com.programacion.distribuida.customers.db.Customer;
import com.programacion.distribuida.customers.dto.CustomerDto;
import com.programacion.distribuida.customers.repo.CustomersRepository;
import com.programacion.distribuida.customers.repo.PurchaseOrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RestController
@RequestMapping("/customers")
@Transactional
public class CustomerRest {

    private static final Logger log = LoggerFactory.getLogger(CustomerRest.class);

    private final CustomersRepository repo;
    private final PurchaseOrderRepository orderRepo;
    private final ModelMapper mapper;


    private final AtomicInteger index = new AtomicInteger(1);

    public CustomerRest(CustomersRepository repo,
                        PurchaseOrderRepository orderRepo,
                        ModelMapper mapper) {
        this.repo      = repo;
        this.orderRepo = orderRepo;
        this.mapper    = mapper;
    }


    @GetMapping
    @CircuitBreaker(name = "customersCb", fallbackMethod = "fallbackFindAll")
    @Retry(name = "customersRetry")
    public List<CustomerDto> findAll() {
        // Simula un fallo cada 5 peticiones
        int cnt = index.getAndIncrement();
        if (cnt % 5 == 0) {
            throw new RuntimeException("Error simulado en findAll, intento " + cnt);
        }
        return repo.findAll().stream()
                .map(c -> mapper.map(c, CustomerDto.class))
                .collect(Collectors.toList());
    }


    public List<CustomerDto> fallbackFindAll(Throwable t) {
        System.err.println("Fallback findAll invocado por: " + t.toString());
        return Collections.emptyList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(c -> mapper.map(c, CustomerDto.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto dto) {
        Customer entity = mapper.map(dto, Customer.class);
        Customer saved = repo.save(entity);
        CustomerDto result = mapper.map(saved, CustomerDto.class);
        return ResponseEntity
                .created(URI.create("/customers/" + saved.getId()))
                .body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody CustomerDto dto) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            Customer entity = mapper.map(dto, Customer.class);
            entity.setId(id);
            // Asegúrate de que dto.version no sea null
            Customer saved = repo.save(entity);
            return ResponseEntity.ok(mapper.map(saved, CustomerDto.class));
        } catch (Exception e) {
            log.error("Error actualizando cliente {}: ", id, e);
            return ResponseEntity
                    .status(500)
                    .body("Error actualizando cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
