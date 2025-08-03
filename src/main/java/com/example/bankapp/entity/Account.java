package com.example.bankapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // For template compatibility
    public String getUsername() {
        return user != null ? user.getUsername() : "Unknown";
    }

    // Constructors
    public Account() {}

    public Account(User user) {
        this.user = user;
        this.balance = 0.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}