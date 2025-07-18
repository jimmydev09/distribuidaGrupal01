package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.repo.AuthorsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/authors")
public class AuthorRest {

    @Autowired
    private AuthorsRepository authorRepository;

    @Value("${server.port}")
    private int serverPort;

    private final AtomicInteger index = new AtomicInteger(1);

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Integer id) {
        return authorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @CircuitBreaker(name = "authorsCb", fallbackMethod = "fallbackFindAll")
    @Retry(name = "authorsRetry")
    public List<Author> findAll() {
        // Simula un fallo cada 7 peticiones
        int cnt = index.getAndIncrement();
        if (cnt % 7 == 0) {
            throw new RuntimeException("Error simulado en findAll, intento " + cnt);
        }
        return authorRepository.findAll().stream()
                .peek(a -> a.setName(a.getName() + " (" + serverPort + ")"))
                .toList();
    }


    public List<Author> fallbackFindAll(Throwable t) {
        System.err.println("Fallback findAll invoked: " + t);
        return Collections.emptyList();
    }


    @GetMapping("/find/{isbn}")
    @CircuitBreaker(name = "authorsCb", fallbackMethod = "fallbackFindByBook")
    @Retry(name = "authorsRetry")
    public List<Author> findByBook(@PathVariable String isbn) {
        // Simula un fallo cada 5 peticiones
        int valor = index.getAndIncrement();
        if (valor % 5 == 0) {
            throw new RuntimeException("Error de prueba en intento " + valor);
        }
        return authorRepository.findByBook(isbn).stream()
                .peek(author -> author.setName(author.getName() + " (" + serverPort + ")"))
                .toList();
    }




    public List<Author> fallbackFindByBook(String isbn, Throwable t) {
        System.err.printf("Fallback invoked for ISBN=%s: %s%n", isbn, t);
        return Collections.emptyList();
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        Author saved = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Integer id,
                                         @RequestBody Author author) {
        author.setId(id);
        Author saved = authorRepository.save(author);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
