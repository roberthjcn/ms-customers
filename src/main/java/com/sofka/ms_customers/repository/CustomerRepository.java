package com.sofka.ms_customers.repository;

import com.sofka.ms_customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c FROM Customer c WHERE c.person.identification = :identification")
    Optional<Customer> findByIdentification(@Param("identification") String identification);
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
    Optional<Customer> findCustomerById(@Param("customerId") UUID customerId);
}
