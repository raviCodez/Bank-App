package com.example.bankapp.controller;

import com.example.bankapp.entity.Transaction;
import com.example.bankapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public String showTransactions(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }
}