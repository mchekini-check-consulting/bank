package com.bank.account.validation;

import com.bank.account.dto.CustomerDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailNotContainsNameValidator implements ConstraintValidator<EmailNotContainsName, CustomerDto> {
    private boolean ignoreCase;

    @Override
    public void initialize(EmailNotContainsName constraintAnnotation) {
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }

    @Override
    public boolean isValid(CustomerDto customerDto, ConstraintValidatorContext context) {
        if (customerDto.getName() == null || customerDto.getEmail() == null) {
            return true;
        }

        if (ignoreCase) {
            return !customerDto.getEmail().toLowerCase().contains(customerDto.getName().toLowerCase());
        } else {
            return !customerDto.getEmail().contains(customerDto.getName());
        }
    }

}
