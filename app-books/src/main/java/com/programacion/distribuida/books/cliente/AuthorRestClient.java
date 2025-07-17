package com.programacion.distribuida.books.cliente;


import com.programacion.distribuida.books.dtos.AuthorDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url="api/authors")
public interface AuthorRestClient {
    @GetExchange("/find/{isbn}")

    List<AuthorDto> findById(@PathVariable("isbn")String isbn);
}
