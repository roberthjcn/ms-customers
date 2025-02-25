package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.entity.Person;
import com.sofka.ms_customers.enums.Gender;
import com.sofka.ms_customers.exceptions.CustomerNotFoundException;
import com.sofka.ms_customers.exceptions.DuplicateIdentificationException;
import com.sofka.ms_customers.mapper.CustomerMapper;
import com.sofka.ms_customers.repository.CustomerRepository;
import com.sofka.ms_customers.repository.PersonRepository;
import com.sofka.ms_customers.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setId(UUID.randomUUID());
        customer.setName("John Doe");
        customer.setGender(Gender.MALE);
        customer.setAge(30);
        customer.setIdentification("123456789");
        customer.setAddress("123 Street");
        customer.setPhone("1234567890");
        customer.setPassword("password123");
        customer.setStatus(true);

        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setGender(customer.getGender());
        customerDTO.setAge(customer.getAge());
        customerDTO.setIdentification(customer.getIdentification());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setStatus(customer.getStatus());
    }

    @Test
    void createCustomer_Success() {
        when(customerMapper.toEntityWithPerson(any(CustomerDTO.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        assertEquals(customerDTO.getIdentification(), result.getIdentification());

        verify(customerMapper, times(1)).toEntityWithPerson(any(CustomerDTO.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).toDTO(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateIdentification() {
        when(customerRepository.findByIdentification(any(String.class))).thenReturn(Optional.of(customer));

        assertThrows(DuplicateIdentificationException.class, () -> {
            customerService.createCustomer(customerDTO);
        });

        verify(customerRepository, times(1)).findByIdentification(any(String.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_Success() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);

        CustomerDTO result = customerService.updateCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        assertEquals(customerDTO.getIdentification(), result.getIdentification());

        verify(customerRepository, times(1)).findById(any(UUID.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).toDTO(any(Customer.class));
    }

    @Test
    void updateCustomer_NotFound() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(customerDTO);
        });

        verify(customerRepository, times(1)).findById(any(UUID.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerByIdentification_Success() {
        when(customerRepository.findByIdentification(any(String.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);

        CustomerDTO result = customerService.getCustomerByIdentification("123456789");

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        assertEquals(customerDTO.getIdentification(), result.getIdentification());

        verify(customerRepository, times(1)).findByIdentification(any(String.class));
        verify(customerMapper, times(1)).toDTO(any(Customer.class));
    }

    @Test
    void getCustomerByIdentification_NotFound() {
        when(customerRepository.findByIdentification(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerByIdentification("123456789");
        });

        verify(customerRepository, times(1)).findByIdentification(any(String.class));
    }

    @Test
    void getAllCustomers_Success() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "asc";

        Person person1 = new Person(UUID.randomUUID(), "John Doe", Gender.MALE, 30, "123456789", "123 Street", "1234567890");
        Person person2 = new Person(UUID.randomUUID(), "Jane Doe", Gender.FEMALE, 25, "987654321", "456 Street", "0987654321");

        List<Customer> customers = Arrays.asList(
                new Customer(UUID.randomUUID(), "password123", true, person1),
                new Customer(UUID.randomUUID(), "password456", true, person2)
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        when(customerMapper.toDTO(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            return new CustomerDTO(customer.getCustomerId(), customer.getPassword(), customer.getStatus(),
                    customer.getId(), customer.getName(), customer.getGender(), customer.getAge(), customer.getIdentification(),
                    customer.getAddress(), customer.getPhone());
        });

        Page<CustomerDTO> result = customerService.getAllCustomers(page, size, sortBy, direction);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("Jane Doe", result.getContent().get(1).getName());

        verify(customerRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAllCustomers_EmptyList() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<Customer> customerPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.getAllCustomers(page, size, sortBy, direction);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(customerRepository, times(1)).findAll(any(Pageable.class));
    }
}