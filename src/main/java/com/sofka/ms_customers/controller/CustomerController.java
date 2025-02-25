package com.sofka.ms_customers.controller;

import com.sofka.ms_customers.dto.CustomerDTO;
import com.sofka.ms_customers.service.CustomerService;
import com.sofka.ms_customers.utils.ApiToResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Customer API", description = "API para gestionar clientes")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiToResponse<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(new ApiToResponse<>("Cliente creado con éxito", customerService.createCustomer(customerDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/{identification}")
    public ResponseEntity<ApiToResponse<CustomerDTO>> getCustomer(@PathVariable String identification) {
        return ResponseEntity.ok(new ApiToResponse<>("Cliente encontrado", customerService.getCustomerByIdentification(identification)));
    }

    @Operation(summary = "Obtener todos los clientes paginados", description = "Devuelve una lista paginada de clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/customers")
    public Page<CustomerDTO> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return customerService.getAllCustomers(page, size, sortBy, direction);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiToResponse<CustomerDTO>> deleteCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(new ApiToResponse<>("Cliente eliminado", customerService.deleteCustomer(customerId)));
    }
}
