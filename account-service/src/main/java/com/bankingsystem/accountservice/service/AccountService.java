package com.bankingsystem.accountservice.service;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public String generateAccountNumber() {
        String accountNumber;
        do{
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        }while(accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    public List<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account findByAccountNumber(String accountNumber) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
        if(account.isEmpty()){
            throw  new RuntimeException("Invalid Account Number");
        }
        return account.get();
    }


//    public Account updateBalance(String accountNumber, BigDecimal amount){
//        Optional<Account> accountOpt = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
//        if (accountOpt.isEmpty()) {
//            throw new RuntimeException("Account not found");
//        }
//        Account account = accountOpt.get();
//        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
//        account.setBalance(currentBalance.add(amount));
//        account.setUpdatedAt(LocalDateTime.now());
//        return accountRepository.save(account);
//    }

    public Account deposit(String AccountNumber, BigDecimal amount){
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByAccountNumber(AccountNumber));
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        account.setBalance(currentBalance.add(amount));
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }


    public Account withdraw(String AccountNumber,BigDecimal amount){
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByAccountNumber(AccountNumber));
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        Account account = accountOptional.get();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void deleteAccount(String accountNumber) {
        accountRepository.delete(accountRepository.findByAccountNumber(accountNumber));
    }
}
