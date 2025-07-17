package com.programacion.distribuida.books.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class BookDto {
    private String isbn;
    private String title;
    private BigDecimal price;
    private List<String> authors;
}
