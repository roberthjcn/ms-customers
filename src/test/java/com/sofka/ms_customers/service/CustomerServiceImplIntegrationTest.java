package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.enums.Gender;
import com.sofka.ms_customers.exceptions.CustomerNotFoundException;
import com.sofka.ms_customers.exceptions.DuplicateIdentificationException;
import com.sofka.ms_customers.repository.CustomerRepository;
import com.sofka.ms_customers.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
public class CustomerServiceImplIntegrationTest {
    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379);
    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerDTO customerDTO;

    @BeforeAll
    static void setUpRedis() {
        System.setProperty("spring.redis.host", redisContainer.getHost());
        System.setProperty("spring.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(UUID.randomUUID());
        customerDTO.setName("John Doe");
        customerDTO.setGender(Gender.MALE);
        customerDTO.setAge(30);
        customerDTO.setIdentification("123456789");
        customerDTO.setAddress("123 Street");
        customerDTO.setPhone("1234567890");
        customerDTO.setPassword("password123");
        customerDTO.setStatus(true);
    }

    @Test
    void createCustomer_Success() {
        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        assertEquals(customerDTO.getIdentification(), result.getIdentification());

        Optional<Customer> savedCustomer = customerRepository.findByIdentification(customerDTO.getIdentification());
        assertTrue(savedCustomer.isPresent());
        assertEquals(customerDTO.getName(), savedCustomer.get().getName());
    }

    @Test
    void createCustomer_DuplicateIdentification() {
        customerService.createCustomer(customerDTO);

        CustomerDTO duplicateCustomerDTO = new CustomerDTO();
        duplicateCustomerDTO.setCustomerId(UUID.randomUUID());
        duplicateCustomerDTO.setName("Jane Doe");
        duplicateCustomerDTO.setGender(Gender.FEMALE);
        duplicateCustomerDTO.setAge(25);
        duplicateCustomerDTO.setIdentification("123456789");
        duplicateCustomerDTO.setAddress("456 Street");
        duplicateCustomerDTO.setPhone("0987654321");
        duplicateCustomerDTO.setPassword("password456");
        duplicateCustomerDTO.setStatus(true);

        assertThrows(DuplicateIdentificationException.class, () -> {
            customerService.createCustomer(duplicateCustomerDTO);
        });
    }

    @Test
    void getCustomerByIdentification_Success() {
        customerService.createCustomer(customerDTO);

        CustomerDTO result = customerService.getCustomerByIdentification(customerDTO.getIdentification());

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        assertEquals(customerDTO.getIdentification(), result.getIdentification());
    }

    @Test
    void getCustomerByIdentification_NotFound() {
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerByIdentification("999999999");
        });
    }
}
