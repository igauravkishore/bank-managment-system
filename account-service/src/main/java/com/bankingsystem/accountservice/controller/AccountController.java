package com.bankingsystem.accountservice.controller;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import com.bankingsystem.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RestClient restClient;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Long userId = account.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("UserId must be provided to create an account.");
        }

        // Verify the user exists before creating an account
        try {
            restClient.get()
                    .uri("http://user-service/api/users/{userId}", userId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a custom error object
        }

        account.setAccountNumber(accountService.generateAccountNumber());
        account.setCreatedDate(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }
}
