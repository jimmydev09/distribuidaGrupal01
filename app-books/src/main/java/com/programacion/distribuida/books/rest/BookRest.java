package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.cliente.AuthorRestClient;
import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dtos.AuthorDto;
import com.programacion.distribuida.books.dtos.BookDto;
import com.programacion.distribuida.books.repo.BookRepository;
import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Collections;
import java.util.List;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
public class BookRest {

    @Autowired
    private BookRepository bookRepository;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestClient.Builder restClientBuilder;

    private AuthorRestClient service;

    @PostConstruct
    private void createAuthorClient() {
        var restClient = restClientBuilder.baseUrl("http://app-authors")
                .build();

        var adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.service = factory.createClient(AuthorRestClient.class);
    }

    // GET--> /books/{isbn}
    // http://localhost:9090/books/1234
    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        var obj = bookRepository.findByIsbnBook(isbn);

        if (obj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookDto ret = new BookDto();
        modelMapper.map(obj.get(), ret);

        try {
            var authors = service.findById(isbn)
                    .stream()
                    .map(AuthorDto::getName)
                    .toList();

            ret.setAuthors(authors);
        } catch (Exception e) {
            ret.setAuthors(Collections.emptyList());
        }

        return ResponseEntity.ok(ret);
    }


    //buscar tyodos
    @GetMapping
    public List<BookDto> findAll() {

        return bookRepository.findAll()
                .stream()
                .map(book -> {
                    var dto = new BookDto();
                    modelMapper.map(book, dto);
                    return dto;
                })
                .map(bookDto -> {
                    try {
                        var authors = service.findById(bookDto.getIsbn())
                                .stream()
                                .map(AuthorDto::getName)
                                .toList();

                        bookDto.setAuthors(authors);
                        return bookDto;
                    } catch (Exception e) {
                        bookDto.setAuthors(Collections.emptyList());
                        return bookDto;
                    }
                })
                .toList();
    }

    // POST /books
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // PUT /books/{isbn}
    @PutMapping(path = "/{isbn}")
    public ResponseEntity<Void> update(@PathVariable("isbn") String isbn, @RequestBody Book book) {
        if (bookRepository.existsById(isbn)) {
            book.setIsbn(isbn);
            bookRepository.save(book);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // http://localhost:9090/books/1234
    @DeleteMapping(path = "/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable("isbn") String isbn) {
        if (!bookRepository.existsById(isbn)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            bookRepository.deleteLineItemsByIsbn(isbn);
            bookRepository.deleteBookAuthorsById(isbn);
            bookRepository.deleteInventoryByIsbn(isbn);
            bookRepository.deleteById(isbn);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

