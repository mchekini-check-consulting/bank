package com.bank.account.dto;

import com.bank.account.validation.EmailNotContainsName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EmailNotContainsName(message = "The email should not contain the customer's name", ignoreCase = false)
public class CustomerDto {
    @NotBlank(message = "Customer name is required", groups = OnCreate.class)
    @Size(min = 2, max = 50, message = "Customer name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email should be valid", groups = OnCreate.class)
    @NotBlank(message = "Email is required")
    private String email;
}
