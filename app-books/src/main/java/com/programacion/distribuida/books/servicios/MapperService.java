package com.programacion.distribuida.books.servicios;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperService {
    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }
}
