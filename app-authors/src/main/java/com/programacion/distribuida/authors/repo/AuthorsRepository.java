package com.programacion.distribuida.authors.repo;


import com.programacion.distribuida.authors.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<Author, Integer> {

    @Query("select i.author from BookAuthor i where i.id.bookIsbn=?1")
    List<Author> findByBook(@Param("isbn") String isbn);

}
