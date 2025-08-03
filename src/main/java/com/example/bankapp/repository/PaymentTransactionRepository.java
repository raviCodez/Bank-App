package com.example.bankapp.repository;

import com.example.bankapp.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByRazorpayPaymentId(String razorpayPaymentId);
    List<PaymentTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}