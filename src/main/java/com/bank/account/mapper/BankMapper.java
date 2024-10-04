package com.bank.account.mapper;

import com.bank.account.dto.AccountDto;
import com.bank.account.dto.CustomerDto;
import com.bank.account.dto.CustomerResponseDto;
import com.bank.account.model.Account;
import com.bank.account.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {
    Customer toEntity(CustomerDto customerDto);
    CustomerDto toDto(Customer customer);
    CustomerResponseDto toResponseDto(Customer customer);
    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);
}
