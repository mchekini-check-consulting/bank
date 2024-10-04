package com.bank.account.repository;

import com.bank.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT a FROM Account a WHERE " +
            "(:accountNumber IS NULL OR :accountNumber = a.accountNumber) AND " +
            "(:balanceMin IS NULL OR :balanceMin <= a.balance) AND " +
            "(:balanceMax IS NULL OR :balanceMax >= a.balance)")
    List<Account> findByCriteria(
            @Param("accountNumber") String accountNumber,
            @Param("balanceMin") BigDecimal balanceMin,
            @Param("balanceMax") BigDecimal balanceMax);
}