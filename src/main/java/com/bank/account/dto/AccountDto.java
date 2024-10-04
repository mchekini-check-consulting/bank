package com.bank.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    @NotBlank(message = "AccountNumber name is required")
    @Size(min = 10, max = 10, message = "AccountNumber must be 10 characters")
    private String accountNumber;

    //@NotBlank(message = "Balance is required")
    private BigDecimal balance;

    @NotBlank(message = "Customer id is required")
    private String customerId;
}
