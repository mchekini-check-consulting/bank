package com.bank.account.service;

import com.bank.account.dto.CustomerDto;
import com.bank.account.mapper.CustomerMapper;
import com.bank.account.model.Customer;
import com.bank.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDto createCustomer(CustomerDto customerDto){
        Customer customer = this.customerMapper.toEntity(customerDto);
        customer.setId(UUID.randomUUID().toString());
        return this.customerMapper.toDto(customerRepository.save(customer));
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

    public List<Customer> searchCustomers(String name, String nameLike, String email) {
        return customerRepository.findCustomerByCriteria(name, nameLike, email);
    }

    public Customer updateCustomer(String id, Customer customer) {
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }
}
