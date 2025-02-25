package com.sofka.ms_customers.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends Person {
    @Column(name = "customer_id", unique = true, nullable = false)
    private UUID customerId = UUID.randomUUID();

    private String password;
    private Boolean status;

    public Customer(UUID customerId, String password, Boolean status, Person person) {
        super(person.getId(), person.getName(), person.getGender(), person.getAge(), person.getIdentification(), person.getAddress(), person.getPhone());
        this.customerId = customerId;
        this.password = password;
        this.status = status;
    }
}
