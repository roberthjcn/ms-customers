package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerByIdentification(String identification);
    List<CustomerDTO> getAllCustomers();
    void deleteCustomer(String customerId);
}
