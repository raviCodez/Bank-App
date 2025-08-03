package com.example.bankapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER_OUT, TRANSFER_IN
    private Double amount;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String description;

    // Constructors
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String type, Double amount, Account account, String description) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}