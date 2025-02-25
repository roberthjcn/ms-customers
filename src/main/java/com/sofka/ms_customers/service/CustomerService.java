package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerByIdentification(String identification);
    Page<CustomerDTO> getAllCustomers(int page, int size, String sortBy, String direction);
    CustomerDTO deleteCustomer(UUID customerId);
}
