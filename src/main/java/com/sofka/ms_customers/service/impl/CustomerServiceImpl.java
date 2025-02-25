package com.sofka.ms_customers.service.impl;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.exceptions.CustomerNotFoundException;
import com.sofka.ms_customers.exceptions.DuplicateIdentificationException;
import com.sofka.ms_customers.mapper.CustomerMapper;
import com.sofka.ms_customers.repository.CustomerRepository;
import com.sofka.ms_customers.repository.PersonRepository;
import com.sofka.ms_customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PersonRepository personRepository;
    private final CustomerMapper customerMapper;


    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        validateDuplicateIdentification(customerDTO.getIdentification());

        customerDTO.setCustomerId(UUID.randomUUID());

        Customer customer = customerMapper.toEntityWithPerson(customerDTO);

        customer = customerRepository.save(customer);

        return customerMapper.toDTO(customer);
    }

    @Override
    @CacheEvict(value = "customers", key = "#customerDTO.identification")
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con identificación: " + customerDTO.getCustomerId()));

        if (customerDTO.getName() != null) existingCustomer.setName(customerDTO.getName());
        if (customerDTO.getGender() != null) existingCustomer.setGender(customerDTO.getGender());
        if (customerDTO.getAge() != 0) existingCustomer.setAge(customerDTO.getAge());
        if (customerDTO.getIdentification() != null) existingCustomer.setIdentification(customerDTO.getIdentification());
        if (customerDTO.getAddress() != null) existingCustomer.setAddress(customerDTO.getAddress());
        if (customerDTO.getPhone() != null) existingCustomer.setPhone(customerDTO.getPhone());

        if (customerDTO.getPassword() != null) existingCustomer.setPassword(customerDTO.getPassword());
        if (customerDTO.getStatus() != null) existingCustomer.setStatus(customerDTO.getStatus());

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return customerMapper.toDTO(updatedCustomer);
    }

    @Override
    @Cacheable(value = "customers", key = "#identification")
    public CustomerDTO getCustomerByIdentification(String identification) {
        return customerRepository.findByIdentification(identification)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con identificación: " + identification));
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customerMapper::toDTO);
    }

    @Override
    @CacheEvict(value = "customers", key = "#result.identification")
    public CustomerDTO deleteCustomer(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con ID: " + customerId));

        customerRepository.deleteById(customerId);

        return customerMapper.toDTO(customer);
    }

    private void validateDuplicateIdentification(String identification) {
        if (customerRepository.findByIdentification(identification).isPresent()) {
            throw new DuplicateIdentificationException("La identificación " + identification + " ya está registrada.");
        }
    }
}
