package com.example.bankapp.repository;

import com.example.bankapp.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    Optional<PaymentOrder> findByRazorpayOrderId(String razorpayOrderId);
    List<PaymentOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
}