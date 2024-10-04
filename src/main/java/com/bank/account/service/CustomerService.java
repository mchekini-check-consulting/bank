package com.bank.account.service;

import com.bank.account.dto.CustomerDto;
import com.bank.account.dto.CustomerResponseDto;
import com.bank.account.mapper.BankMapper;
import com.bank.account.model.Customer;
import com.bank.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BankMapper bankMapper;

    public CustomerService(BankMapper bankMapper, CustomerRepository customerRepository) {
        this.bankMapper = bankMapper;
        this.customerRepository = customerRepository;
    }

    public CustomerDto createCustomer(CustomerDto customerDto){
        Customer customer = this.bankMapper.toEntity(customerDto);
        customer.setId(UUID.randomUUID().toString());
        return this.bankMapper.toDto(customerRepository.save(customer));
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return this.customerRepository.findByEmail(email);
    }

    public void deleteCustomerById(String id) {
        customerRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return customerRepository.existsById(id);
    }

    public List<CustomerResponseDto> searchCustomers(String name, String nameLike, String email) {
        return customerRepository.findCustomerByCriteria(name, nameLike, email)
                .stream()
                .map(this.bankMapper::toResponseDto)
                .toList();
    }

    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        Customer customer = this.bankMapper.toEntity(customerDto);
        customer.setId(id);
        return this.bankMapper.toDto(customerRepository.save(customer));
    }

    public Optional<CustomerDto> getCustomerById(String id) {
        return customerRepository.findById(id)
                .map(bankMapper::toDto);
    }
}