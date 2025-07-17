package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.db.Author;
import com.programacion.distribuida.authors.repo.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/authors")
public class AuthorRest {

    @Autowired
    private AuthorsRepository authorRepository;

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Integer id) {
        return authorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @GetMapping("/find/{isbn}")
    public List<Author> findByBook(@PathVariable String isbn) {
        return authorRepository.findByBook(isbn).stream()
                .peek(author -> author.setName(author.getName() + " (" + serverPort + ")"))
                .toList();
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        Author saved = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // UPDATE (upsert)
    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Integer id,
                                         @RequestBody Author author) {
        author.setId(id);
        Author saved = authorRepository.save(author);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}