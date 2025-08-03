package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }

        User user = new User(username, passwordEncoder.encode(password));
        user = userRepository.save(user);

        // Create account for user
        Account account = new Account(user);
        accountRepository.save(account);

        return user;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}