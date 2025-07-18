package com.programacion.distribuida.customers.repo;

import com.programacion.distribuida.customers.db.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Integer> {
    @Query("select c from LineItem c where c.id = ?1")
    Optional<LineItem> findById(Integer id);
}
