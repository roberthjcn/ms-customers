package com.sofka.ms_customers.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue requestQueue() {
        return new Queue("customer.validation.request", false);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue("customer.validation.response", false);
    }
}
