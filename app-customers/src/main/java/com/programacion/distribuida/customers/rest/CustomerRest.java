// app-customers/src/main/java/com/programacion/distribuida/customers/rest/CustomerRest.java
package com.programacion.distribuida.customers.rest;

import com.programacion.distribuida.customers.db.Customer;
import com.programacion.distribuida.customers.dto.CustomerDto;
import com.programacion.distribuida.customers.repo.CustomersRepository;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@Transactional
public class CustomerRest {

    private final CustomersRepository repo;
    private final ModelMapper mapper;

    public CustomerRest(CustomersRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
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
    public ResponseEntity<CustomerDto> update(@PathVariable Integer id,
                                              @RequestBody CustomerDto dto) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Customer entity = mapper.map(dto, Customer.class);
        entity.setId(id);
        Customer saved = repo.save(entity);
        return ResponseEntity.ok(mapper.map(saved, CustomerDto.class));
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
