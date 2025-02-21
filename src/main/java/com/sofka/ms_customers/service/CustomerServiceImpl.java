package com.sofka.ms_customers.service;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.entity.Customer;
import com.sofka.ms_customers.entity.Person;
import com.sofka.ms_customers.exceptions.CustomerNotFoundException;
import com.sofka.ms_customers.mapper.CustomerMapper;
import com.sofka.ms_customers.repository.CustomerRepository;
import com.sofka.ms_customers.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PersonRepository personRepository;
    private final CustomerMapper customerMapper;


    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Person person = createPersonFromDTO(customerDTO);
        person = personRepository.save(person);

        Customer customer = customerMapper.toEntityWithPerson(customerDTO);
        customer.setPerson(person);
        customer = customerRepository.save(customer);

        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(customerDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con identificación: " + customerDTO.getCustomerId()));

        Person person = existingCustomer.getPerson();

        if (customerDTO.getName() != null) person.setName(customerDTO.getName());
        if (customerDTO.getGender() != null) person.setGender(customerDTO.getGender());
        if (customerDTO.getAge() != 0) person.setAge(customerDTO.getAge());
        if (customerDTO.getIdentification() != null) person.setIdentification(customerDTO.getIdentification());
        if (customerDTO.getAddress() != null) person.setAddress(customerDTO.getAddress());
        if (customerDTO.getPhone() != null) person.setPhone(customerDTO.getPhone());

        personRepository.save(person);

        if (customerDTO.getPassword() != null) existingCustomer.setPassword(customerDTO.getPassword());
        if (customerDTO.getStatus() != null) existingCustomer.setStatus(customerDTO.getStatus());

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return customerMapper.toDTO(updatedCustomer);
    }

    @Override
    public CustomerDTO getCustomerByIdentification(String identification) {
        return customerRepository.findByIdentification(identification)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con identificación: " + identification));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Cliente no encontrado con ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    private Person createPersonFromDTO(CustomerDTO customerDTO) {
        Person person = new Person();
        person.setName(customerDTO.getName());
        person.setGender(customerDTO.getGender());
        person.setAge(customerDTO.getAge());
        person.setIdentification(customerDTO.getIdentification());
        person.setAddress(customerDTO.getAddress());
        person.setPhone(customerDTO.getPhone());
        return person;
    }
}
