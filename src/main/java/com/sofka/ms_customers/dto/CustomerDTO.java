package com.sofka.ms_customers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sofka.ms_customers.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    private UUID customerId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean status;

    private UUID id;
    private String name;
    private Gender gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
