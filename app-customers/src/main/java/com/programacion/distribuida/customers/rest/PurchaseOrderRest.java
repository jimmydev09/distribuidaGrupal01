package com.programacion.distribuida.customers.rest;


import com.programacion.distribuida.customers.cliente.BookRestClient;
import com.programacion.distribuida.customers.db.Customer;
import com.programacion.distribuida.customers.db.PurchaseOrder;
import com.programacion.distribuida.customers.dto.CustomerDto;
import com.programacion.distribuida.customers.dto.PurchaseOrderDto;
import com.programacion.distribuida.customers.repo.CustomersRepository;
import com.programacion.distribuida.customers.repo.PurchaseOrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Transactional
public class PurchaseOrderRest {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderRest.class);

    @Value("${server.port}")
    private Integer httpPort;

    @Value("${services.books.url}")
    private String booksServiceUrl;

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private CustomersRepository customerRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestClient.Builder clientBuilder;

    private BookRestClient createBookClient() {
        RestClient restClient = clientBuilder
                .baseUrl(booksServiceUrl) //.baseUrl(booksServiceUrl + "/books")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BookRestClient.class);
    }

    private PurchaseOrderDto map(PurchaseOrder source) {
        var dto = mapper.map(source, PurchaseOrderDto.class);
        BookRestClient client = createBookClient();

        dto.getLineItems().forEach(item -> {
            try {
                var book = client.findByBook(item.getIsbn());
                item.setTitle(book.getTitle());
                item.setPrice(book.getPrice());
                item.setAuthors(book.getAuthors());
            } catch (Exception e) {
                log.warn("No se pudo obtener el libro con ISBN {}", item.getIsbn(), e);
                item.setTitle("Desconocido");
                item.setPrice(BigDecimal.ZERO);
                item.setAuthors(Collections.emptyList());
            }
        });

        return dto;
    }

    // http://localhost:7070/orders/customer/1
    @GetMapping("/customer/{customerId}")
    public List<PurchaseOrderDto> ordersByCustomer(@PathVariable Integer customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    // http://localhost:7070/orders/1
    @GetMapping("/{orderId}")
    public ResponseEntity<PurchaseOrderDto> orderDetail(@PathVariable Integer orderId) {
        return repository.findById(orderId)
                .map(this::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    // http://localhost:7070/orders
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody PurchaseOrder order) {
        order.setId(null);
        if (order.getCustomer() != null && order.getCustomer().getId() == null) {
            order.getCustomer().setId(null);
            Customer savedCustomer = customerRepository.save(order.getCustomer());
            order.setCustomer(savedCustomer);
        }
        else if (order.getCustomer() != null && order.getCustomer().getId() != null) {
            Customer existingCustomer = customerRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer no encontrado"));
            order.setCustomer(existingCustomer);
        }

        repository.save(order);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> update(@PathVariable Integer orderId, @RequestBody PurchaseOrder order) {
        if (!repository.existsById(orderId)) {
            return ResponseEntity.status(404).build();
        }
        order.setId(orderId);
        if (order.getCustomer() != null) {
            if (order.getCustomer().getId() == null) {
                order.getCustomer().setId(null);
                Customer savedCustomer = customerRepository.save(order.getCustomer());
                order.setCustomer(savedCustomer);
            } else {
                Customer existingCustomer = customerRepository.findById(order.getCustomer().getId())
                        .orElseThrow(() -> new RuntimeException("Customer no encontrado"));
                order.setCustomer(existingCustomer);
            }
        }
        if (order.getLineItems() != null) {
            order.getLineItems().forEach(item -> item.setId(null));
        }

        repository.save(order);
        return ResponseEntity.ok().build();
    }

    // http://localhost:7070/orders/id
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    // listar todos
    // http://localhost:7070/orders
    @GetMapping
    public List<PurchaseOrderDto> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // DELETE an order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        if (!repository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }

}
