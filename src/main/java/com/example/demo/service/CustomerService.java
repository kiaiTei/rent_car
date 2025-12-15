package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Customer;

@Service
public class CustomerService {

    // 仮データ（DB未接続でも動かすため）
    public List<Customer> findAll() {
        return new ArrayList<>();
    }

    public Customer findById(int customerId) {
        return new Customer();
    }
}