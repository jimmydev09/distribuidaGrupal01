package com.programacion.distribuida.customers.repo;

import com.programacion.distribuida.customers.db.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer> {
    @Query("select c from Customer c where c.id = ?1")
    Optional<Customer> findById(Integer id);
}