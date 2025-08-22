package com.bankingsystem.transactionservice.service;

import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    private RestClient restClient;
    private final TransactionRepository transactionRepository;
    private static final String ACCOUNT_URL = "http://account-service/api/accounts";

    public TransactionService(RestClient restClient, TransactionRepository transactionRepository) {
        this.restClient = restClient;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction processTransfer(String fromAccount, String toAccount, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setFromAccountNumber(fromAccount);
        transaction.setToAccountNumber(toAccount);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());

        try{
            restClient.post()
                    .uri(ACCOUNT_URL + "/{fromAccount}/withdraw?amount={amount}", fromAccount, amount)
                    .retrieve()
                    .toBodilessEntity();

            restClient.post()
                    .uri(ACCOUNT_URL + "/{toAccount}/deposit?amount={amount}", toAccount, amount)
                    .retrieve()
                    .toBodilessEntity();
            transaction.setTransactionStatus(Transaction.TransactionStatus.SUCCESS);
            return transactionRepository.save(transaction);
        }catch(Exception e){
            try {
                restClient.post()
                        .uri(ACCOUNT_URL + "/{fromAccount}/deposit?amount={amount}", fromAccount, amount)
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }

            throw new RuntimeException("Transfer failed. Rollback performed: " + e.getMessage());
        }
    }
}
