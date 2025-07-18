package com.programacion.distribuida.customers.cliente;

import com.programacion.distribuida.customers.dto.BookDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url="api/books")
public interface BookRestClient {
    @GetExchange("/books/{isbn}")
    BookDto findByBook(@PathVariable("isbn") String isbn);
}
