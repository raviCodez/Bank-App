package com.example.bankapp.service;

import com.example.bankapp.entity.PaymentOrder;
import com.example.bankapp.entity.PaymentTransaction;
import com.example.bankapp.repository.PaymentOrderRepository;
import com.example.bankapp.repository.PaymentTransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private AccountService accountService;

    @Transactional
    public PaymentOrder createPaymentOrder(Long userId, Double amount) throws RazorpayException {
        // Create Razorpay order
        Order razorpayOrder = razorpayService.createOrder(amount);

        // Save order details in database
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setRazorpayOrderId(razorpayOrder.get("id"));
        paymentOrder.setAmount(amount);
        paymentOrder.setCurrency("INR");
        paymentOrder.setStatus("CREATED");
        paymentOrder.setUserId(userId);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Transactional
    public boolean verifyAndProcessPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        // Verify payment signature
        if (!razorpayService.verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature)) {
            return false;
        }

        // Find the payment order
        PaymentOrder paymentOrder = paymentOrderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElse(null);

        if (paymentOrder == null) {
            return false;
        }

        // Create payment transaction record
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setRazorpayPaymentId(razorpayPaymentId);
        transaction.setRazorpayOrderId(razorpayOrderId);
        transaction.setRazorpaySignature(razorpaySignature);
        transaction.setAmount(paymentOrder.getAmount());
        transaction.setStatus("SUCCESS");
        transaction.setUserId(paymentOrder.getUserId());

        paymentTransactionRepository.save(transaction);

        // Update payment order status
        paymentOrder.setStatus("PAID");
        paymentOrder.setUpdatedAt(LocalDateTime.now());
        paymentOrderRepository.save(paymentOrder);

        // Add money to user's account
        accountService.addMoneyToAccount(paymentOrder.getUserId(), paymentOrder.getAmount());

        return true;
    }

    public List<PaymentTransaction> getUserPaymentHistory(Long userId) {
        return paymentTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}