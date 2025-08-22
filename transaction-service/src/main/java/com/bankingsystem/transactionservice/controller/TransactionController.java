package com.bankingsystem.transactionservice.controller;

import com.bankingsystem.transactionservice.Dtos.TransferRequest;
import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> createTransaction(@RequestBody TransferRequest transactionRequest) {
        try {
            Transaction savedTransaction = transactionService.processTransfer(
                    transactionRequest.getFromAccount(),
                    transactionRequest.getToAccount(),
                    transactionRequest.getAmount()
            );

            return ResponseEntity.ok(savedTransaction);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transaction failed: " + e.getMessage());
        }
    }
}
