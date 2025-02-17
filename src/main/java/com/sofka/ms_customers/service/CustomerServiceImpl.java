package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.entity.Person;
import com.sofka.ms_customers.mapper.CustomerMapper;
import com.sofka.ms_customers.repository.CustomerRepository;
import com.sofka.ms_customers.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PersonRepository personRepository;
    private final CustomerMapper customerMapper;


    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Person person = new Person();
        person.setName(customerDTO.getName());
        person.setGender(customerDTO.getGender());
        person.setAge(customerDTO.getAge());
        person.setIdentification(customerDTO.getIdentification());
        person.setAddress(customerDTO.getAddress());
        person.setPhone(customerDTO.getPhone());
        person = personRepository.save(person);

        Customer customer = customerMapper.toEntityWithPerson(customerDTO);
        customer.setPerson(person);
        customer = customerRepository.save(customer);

        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO getCustomerByIdentification(String identification) {
        System.out.println("LLega: "+identification);
        return customerRepository.findByIdentification(identification)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}
