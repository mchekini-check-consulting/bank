package com.bank.account.controllers;

import com.bank.account.dto.CustomerDto;
import com.bank.account.dto.OnCreate;
import com.bank.account.exceptions.CustomerNotFoundException;
import com.bank.account.exceptions.EmailAlreadyExistsException;
import com.bank.account.mapper.CustomerMapper;
import com.bank.account.model.Customer;
import com.bank.account.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/customers")
@RestController
@Validated
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @Operation(summary = "service to create custumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The created customer"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/")
  //  public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto customerCreate) throws EmailAlreadyExistsException {
    public ResponseEntity<CustomerDto> create(@RequestBody @Validated(OnCreate.class) CustomerDto customerCreate) throws EmailAlreadyExistsException {
        this.checkCustomerByEmail(customerCreate);
        CustomerDto customerDto = this.customerService.createCustomer(customerCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @Operation(summary = "service to delete custumer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id){
        this.checkCustomerById(id);
        this.customerService.deleteCustomerById(id);
    }

    @Operation(summary = "service to search custumers by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of customers with pagination information"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> search(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String nameLike,
                                                    @RequestParam(required = false) String email) {
        List<CustomerDto> customerDtos = customerService.searchCustomers(name, nameLike, email)
                .stream()
                .map(c -> this.customerMapper.toDto(c))
                .collect(Collectors.toList());

        return new ResponseEntity<>(customerDtos, HttpStatus.OK) ;
    }

    @Operation(summary = "service to update a custumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable String id, @RequestBody CustomerDto customerUpdate) {
        this.checkCustomerById(id);

        CustomerDto updatedCustomer = this.customerMapper.toDto(
                this.customerService.updateCustomer(id, this.customerMapper.toEntity(customerUpdate)));
       return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "endpoint to get a custumer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A customer with the provided id"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable String id) {
        Customer customer = this.customerService.getCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));

        CustomerDto customerDto = this.customerMapper.toDto(customer);
        return ResponseEntity.ok(customerDto);
    }

    private void checkCustomerById(String id) {
        if (!this.customerService.existsById(id)) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
    }

    private void checkCustomerByEmail(CustomerDto customerCreate) throws EmailAlreadyExistsException {
        if(this.customerService.findCustomerByEmail(customerCreate.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("email " + customerCreate.getEmail() + " already exists!");
        }
    }

}
