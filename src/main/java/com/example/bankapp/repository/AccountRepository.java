package com.example.bankapp.repository;

import com.example.bankapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);
    Optional<Account> findByUserUsername(String username);
}