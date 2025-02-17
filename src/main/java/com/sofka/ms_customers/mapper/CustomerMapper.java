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
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "phone", source = "person.phone")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", source = "status")
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "person.name", source = "name")
    @Mapping(target = "person.gender", source = "gender")
    @Mapping(target = "person.age", source = "age")
    @Mapping(target = "person.identification", source = "identification")
    @Mapping(target = "person.address", source = "address")
    @Mapping(target = "person.phone", source = "phone")
    Customer toEntityWithPerson(CustomerDTO customerDTO);
}
