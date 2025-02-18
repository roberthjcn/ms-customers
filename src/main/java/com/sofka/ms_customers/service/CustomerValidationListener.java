package com.sofka.ms_customers.service;

import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.repository.CustomerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class CustomerValidationListener {
    private final CustomerRepository customerRepository;
    private final RabbitTemplate rabbitTemplate;

    public CustomerValidationListener(CustomerRepository customerRepository, RabbitTemplate rabbitTemplate) {
        this.customerRepository = customerRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "customer.validation.request")
    public void validateCustomer(String customerId) {
        UUID uuid = UUID.fromString(customerId);
        Optional<Customer> exists = customerRepository.findCustomerById(uuid);
        rabbitTemplate.convertAndSend("customer.validation.response", customerId + ":" + exists.isEmpty());
    }
}
