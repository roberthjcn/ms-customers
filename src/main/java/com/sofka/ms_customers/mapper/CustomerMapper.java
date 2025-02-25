package com.sofka.ms_customers.mapper;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "identification", source = "identification")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "identification", source = "identification")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    Customer toEntityWithPerson(CustomerDTO customerDTO);
}
