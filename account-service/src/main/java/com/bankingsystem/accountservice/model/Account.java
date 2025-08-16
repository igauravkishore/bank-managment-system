package com.bankingsystem.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private Long userId;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT
    }


}
