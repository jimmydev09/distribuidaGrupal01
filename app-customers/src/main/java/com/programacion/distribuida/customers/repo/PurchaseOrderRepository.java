package com.programacion.distribuida.customers.repo;

import com.programacion.distribuida.customers.db.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    @Query("select p from PurchaseOrder p where p.customer.id = ?1")
    List<PurchaseOrder> findByCustomerId(Integer customerId);
}
