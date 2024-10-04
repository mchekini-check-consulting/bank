package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.dto.CustomerDto;
import com.bank.account.mapper.BankMapper;
import com.bank.account.model.Account;
import com.bank.account.model.Customer;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final BankMapper bankMapper;

    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository,
                          BankMapper bankMapper) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.bankMapper = bankMapper;
    }

    public Optional<CustomerDto> findCustomerById(String id) {
        return this.customerRepository.findById(id).map(this.bankMapper::toDto);
    }

    public AccountDto create(AccountDto accountCreate) {
        Account accountToSave = this.bankMapper.toEntity(accountCreate);
        accountToSave.setId(UUID.randomUUID().toString());
        Customer customer = this.customerRepository.findById(accountCreate.getCustomerId()).get();
        accountToSave.setCustomer(customer);
        AccountDto savedAccountDto = this.bankMapper.toDto(accountRepository.save(accountToSave));
        savedAccountDto.setCustomerId(customer.getId());
        return savedAccountDto;
    }

    public void deleteById(String id) {
        this.accountRepository.deleteById(id);
    }

    public Optional<AccountDto> findById(String id) {
        return this.accountRepository.findById(id).map(this.bankMapper::toDto);
    }

    public AccountDto update(String id, AccountDto accountUpdate) {
        Account account = this.bankMapper.toEntity(accountUpdate);
        account.setId(id);
        return this.bankMapper.toDto(this.accountRepository.save(account));
    }

    public List<AccountDto> search(String accountNumber, BigDecimal balanceMin, BigDecimal balanceMax) {
        List<Account> accounts = this.accountRepository.findByCriteria(accountNumber, balanceMin, balanceMax);
        return accounts.stream().map(this.bankMapper::toDto).toList();
    }
}