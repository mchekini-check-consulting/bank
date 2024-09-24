package com.bank.account.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Customer {
    @Id
    private String id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();
}
