package com.zenil.store.customer.service;

import com.zenil.store.customer.repository.entity.Customer;
import com.zenil.store.customer.repository.entity.Region;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    List<Customer> findByRegion(Region region);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer deleteCustomer(Customer customer);
    Customer getCustomer(Long id);
}
