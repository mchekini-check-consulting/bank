package com.bank.account.repository;

import com.bank.account.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);

    Customer findByNameAndEmail(String name, String email);

    Customer findByName(String name);

    @Query("select * from customer c where "
        + " ( :name is NULL OR c.name = :name ) AND "
        + " ( :nameLike is NULL OR c.name LIKE :name ) AND"
        + " ( :email is NULL OR c.email = :email )"
    )
    List<Customer> findCustomerByCriteria(@Param("name") String name,
                                          @Param("nameLike")String nameLike,
                                          @Param("email")String email);
}
