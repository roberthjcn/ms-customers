package com.sofka.ms_customers.repository;

import com.sofka.ms_customers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query(value = "SELECT c.customer_id, c.password, c.status, p.id, p.name, p.gender, p.age, p.identification, p.address, p.phone " +
            "FROM customer c JOIN person p ON c.id = p.id WHERE p.identification = :identification", nativeQuery = true)
    Optional<Customer> findByIdentification(@Param("identification") String identification);
    Page<Customer> findAll(Pageable pageable);
}
