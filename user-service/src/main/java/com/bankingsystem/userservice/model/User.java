package com.bankingsystem.userservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private UUID userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}
