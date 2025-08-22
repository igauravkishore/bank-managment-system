package com.bankingsystem.accountservice.controller;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import com.bankingsystem.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
                    .uri("http://user-service/api/users/id/{userId}", userId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a custom error object
        }

        account.setAccountNumber(accountService.generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }

//    @PutMapping("/{accountNumber}/balance")
//    public ResponseEntity<Account> updateAccountBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
//        try{
//            Account updatedAccount = accountService.updateBalance(accountNumber, amount);
//            return ResponseEntity.ok(updatedAccount);
//        }catch(RuntimeException e){
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        try{
            Account depositedAccount = accountService.deposit(accountNumber, amount);
            return ResponseEntity.ok(depositedAccount);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        try {
            Account withdrawedAccount = accountService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(withdrawedAccount);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAlAccounts() {
      return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        try{
            List<Account> accounts = accountService.findByUserId(userId);
            if(accounts.isEmpty()) {
                return ResponseEntity.notFound().build();
            }else return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
