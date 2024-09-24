package com.bank.account.mapper;

import com.bank.account.dto.CustomerDto;
import com.bank.account.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerDto customerDto);
    CustomerDto toDto(Customer customer);
}
