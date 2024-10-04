package com.bank.account.controllers;

import com.bank.account.dto.AccountDto;
import com.bank.account.exceptions.CustomerNotFoundException;
import com.bank.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/v1/accounts")
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "endpoint to create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The created account"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    public ResponseEntity<AccountDto> create(@RequestBody @Valid AccountDto accountCreate) throws CustomerNotFoundException {
        this.validateReferenceToCustomer(accountCreate.getCustomerId());
        AccountDto createdAccount = this.accountService.create(accountCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @Operation(summary = "endpoint to delete account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws AccountNotFoundException {
        this.checkIfAccountExists(id);
        this.accountService.deleteById(id);
    }

    @Operation(summary = "endpoint to update an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> update(@PathVariable String id, @RequestBody AccountDto accountUpdate) throws AccountNotFoundException {
        this.checkIfAccountExists(id);
        AccountDto updatedAccount = this.accountService.update(id, accountUpdate);
        return ResponseEntity.ok(updatedAccount);
    }

    @Operation(summary = "endpoint to get an account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A customer with the provided id"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> read(@PathVariable String id) throws AccountNotFoundException {
        AccountDto readAccount = this.accountService.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
        return ResponseEntity.ok(readAccount);
    }

    @Operation(summary = "endpoint to search accounts by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of customers with pagination information"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorised"),
            @ApiResponse(responseCode = "403", description = "Forbiden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<AccountDto>> search(@RequestParam String accountNumber,
                                                   @RequestParam BigDecimal balanceMin,
                                                   @RequestParam BigDecimal balanceMax) {
        List<AccountDto> accountDtos = this.accountService.search(accountNumber, balanceMin, balanceMax);
        return ResponseEntity.ok(accountDtos);
    }

    private void checkIfAccountExists(String id) throws AccountNotFoundException {
        if (this.accountService.findById(id).isEmpty()) {
            throw new AccountNotFoundException("Account with ID " + id + " not found");
        }
    }

    private void validateReferenceToCustomer(String id) throws CustomerNotFoundException {
        if (this.accountService.findCustomerById(id).isEmpty()) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
    }
}
