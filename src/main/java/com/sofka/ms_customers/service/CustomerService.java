package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerByIdentification(String identification);
    List<CustomerDTO> getAllCustomers();
    void deleteCustomer(UUID customerId);
}
