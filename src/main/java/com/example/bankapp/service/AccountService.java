package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUserUsername(username);
    }

    public Optional<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public void deposit(String username, Double amount) {
        Account account = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction("Deposit", amount, account, "Deposit to account");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(String username, Double amount) {
        Account account = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction("Withdraw", amount, account, "Withdrawal from account");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void transfer(String fromUsername, String toUsername, Double amount) {
        Account fromAccount = findByUsername(fromUsername)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Record transactions
        Transaction outTransaction = new Transaction("Transfer Out", amount, fromAccount, "Transfer to " + toUsername);
        Transaction inTransaction = new Transaction("Transfer In", amount, toAccount, "Transfer from " + fromUsername);

        transactionRepository.save(outTransaction);
        transactionRepository.save(inTransaction);
    }

    @Transactional
    public void addMoneyToAccount(Long userId, Double amount) {
        Account account = findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction("Online Payment", amount, account, "Money added via Razorpay");
        transactionRepository.save(transaction);
    }
}