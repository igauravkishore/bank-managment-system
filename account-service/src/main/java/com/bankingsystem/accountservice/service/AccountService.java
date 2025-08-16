package com.bankingsystem.accountservice.service;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RestClient restClient;

    public String generateAccountNumber() {
        String accountNumber;
        do{
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        }while(accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

}
