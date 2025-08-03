package com.example.bankapp.service;

import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByUsername(String username) {
        return transactionRepository.findByAccountUserUsernameOrderByTimestampDesc(username);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }
}