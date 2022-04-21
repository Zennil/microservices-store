package com.zenil.store.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenil.store.customer.repository.entity.Customer;
import com.zenil.store.customer.repository.entity.Region;
import com.zenil.store.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerRest {

    @Autowired
    CustomerService customerService;

    // -----------------------------Retrieve All Customers Or by Region-------------------------------------
    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId", required = false) Long regionId) {
        List<Customer> customers = new ArrayList<>();
        if (regionId == null) {
            customers = customerService.findAll();
            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            Region region = new Region();
            region.setId(regionId);
            customers = customerService.findByRegion(region);
            if (customers.isEmpty()) {
                log.error("Customers with Region id {} not found.", regionId);
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(customers);
    }

    // -----------------------------Retrieve Single Customer --------------------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
        log.info("Fetching Customer with id {}", id);
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            log.error("Customer with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    // -------------------Create a Customer-------------------------------------------
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer newCustomer, BindingResult bindingResult) {
        log.info("Creating Customer: {}", newCustomer);
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
        }
        Customer customer = customerService.createCustomer(newCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    // ------------------- Update a Customer ------------------------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        log.info("Updating Customer with id {}", id);
        Customer customerDB = customerService.getCustomer(id);
        if (customerDB == null) {
            log.error("Unable to update, Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customerDB = customerService.updateCustomer(customer);
        return ResponseEntity.ok(customerDB);
    }

    // ------------------- Delete a Customer-----------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id) {
        log.info("Fetching and Deleting Customer with id {}", id);
        Customer customerBD = customerService.getCustomer(id);
        if (customerBD == null) {
            log.error("Unable to delete. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        customerBD = customerService.deleteCustomer(customerBD);
        return ResponseEntity.ok(customerBD);
    }

    private String formatMessage(BindingResult bindingResult) {
        List<Map<String, String>> errors = bindingResult.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
