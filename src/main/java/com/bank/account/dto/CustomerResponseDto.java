package com.bank.account.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerResponseDto extends CustomerDto {
    private String id;
}