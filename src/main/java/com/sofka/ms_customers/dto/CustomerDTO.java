package com.sofka.ms_customers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String customerId;
    private String password;
    private String status;

    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
