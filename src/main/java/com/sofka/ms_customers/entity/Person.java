package com.sofka.ms_customers.entity;

import com.sofka.ms_customers.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
