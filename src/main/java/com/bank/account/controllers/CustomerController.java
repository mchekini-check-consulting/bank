package com.bank.account.controllers;

import com.bank.account.dto.CustomerDto;
import com.bank.account.dto.CustomerResponseDto;
import com.bank.account.exceptions.CustomerNotFoundException;
import com.bank.account.exceptions.EmailAlreadyExistsException;
import com.bank.account.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "endpoint to create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The created customer"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/")
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto customerCreate) throws EmailAlreadyExistsException {
        this.validateCustomerEmailUniqueness(customerCreate);
        CustomerDto customerDto = this.customerService.createCustomer(customerCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @Operation(summary = "endpoint to delete customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id) throws CustomerNotFoundException {
        this.checkIfCustomerExists(id);
        this.customerService.deleteCustomerById(id);
    }

    @Operation(summary = "endpoint to search customers by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of customers with pagination information"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/search")
    public ResponseEntity<List<CustomerResponseDto>> search(@RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String nameLike,
                                                                @RequestParam(required = false) String email) {
        List<CustomerResponseDto> customerResponseDto = customerService.searchCustomers(name, nameLike, email);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK) ;
    }

    @Operation(summary = "endpoint to update a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable String id, @RequestBody CustomerDto customerUpdate) throws CustomerNotFoundException {
        this.checkIfCustomerExists(id);
        CustomerDto updatedCustomer = this.customerService.updateCustomer(id, customerUpdate);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "endpoint to get a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A customer with the provided id"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> read(@PathVariable String id) throws CustomerNotFoundException {
        CustomerDto customerDto = this.customerService.getCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
        return ResponseEntity.ok(customerDto);
    }

    private void checkIfCustomerExists(String id) throws CustomerNotFoundException {
        if (!this.customerService.existsById(id)) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
    }

    private void validateCustomerEmailUniqueness(CustomerDto customerCreate) throws EmailAlreadyExistsException {
        if(this.customerService.findCustomerByEmail(customerCreate.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("email " + customerCreate.getEmail() + " already exists!");
        }
    }
}
