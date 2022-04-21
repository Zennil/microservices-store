package com.zenil.store.customer.repository;

import com.zenil.store.customer.repository.entity.Customer;
import com.zenil.store.customer.repository.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByNumberId(String numberId);
    List<Customer> findByLastName(String lastName);
    List<Customer> findByRegion(Region region);
}
