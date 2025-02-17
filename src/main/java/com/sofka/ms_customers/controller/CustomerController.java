package com.sofka.ms_customers.controller;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.service.CustomerService;
import com.sofka.ms_customers.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(new ApiResponse<>("Cliente creado con éxito", customerService.createCustomer(customerDTO)));
    }

    @GetMapping("/{identification}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(@PathVariable String identification) {
        return ResponseEntity.ok(new ApiResponse<>("Cliente encontrado", customerService.getCustomerByIdentification(identification)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getCustomers() {
        return ResponseEntity.ok(new ApiResponse<>("Lista de clientes", customerService.getAllCustomers()));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(new ApiResponse<>("Cliente eliminado", null));
    }
}
