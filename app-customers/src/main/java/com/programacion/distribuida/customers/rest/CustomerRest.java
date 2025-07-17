package com.programacion.distribuida.customers.rest;

import com.programacion.distribuida.customers.db.Customer;
import com.programacion.distribuida.customers.dto.CustomerDto;
import com.programacion.distribuida.customers.repo.CustomersRepository;
import com.programacion.distribuida.customers.repo.PurchaseOrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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

    public CustomerRest(CustomersRepository repo,
                        PurchaseOrderRepository orderRepo,
                        ModelMapper mapper) {
        this.repo      = repo;
        this.orderRepo = orderRepo;
        this.mapper    = mapper;
    }

    // READ all
    @GetMapping
    public List<CustomerDto> findAll() {
        return repo.findAll().stream()
                .map(c -> mapper.map(c, CustomerDto.class))
                .collect(Collectors.toList());
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(c -> mapper.map(c, CustomerDto.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto dto) {
        Customer entity = mapper.map(dto, Customer.class);
        Customer saved = repo.save(entity);
        CustomerDto result = mapper.map(saved, CustomerDto.class);
        return ResponseEntity
                .created(URI.create("/customers/" + saved.getId()))
                .body(result);
    }

    // UPDATE
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

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
