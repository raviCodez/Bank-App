// Update PaymentController.java - Replace the existing file
package com.example.bankapp.controller;

import com.example.bankapp.entity.PaymentOrder;
import com.example.bankapp.entity.PaymentTransaction;
import com.example.bankapp.service.PaymentService;
import com.example.bankapp.service.UserService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @GetMapping("/add-money")
    public String showAddMoneyPage(Model model, Authentication authentication) {
        model.addAttribute("razorpayKeyId", razorpayKeyId);
        return "add-money";
    }

    @PostMapping("/create-order")
    @ResponseBody
    public PaymentOrder createOrder(@RequestParam Double amount, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            return paymentService.createPaymentOrder(userId, amount);
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment order", e);
        }
    }

    @PostMapping("/verify-payment")
    @ResponseBody
    public String verifyPayment(@RequestParam String razorpay_order_id,
                                @RequestParam String razorpay_payment_id,
                                @RequestParam String razorpay_signature) {
        boolean isValid = paymentService.verifyAndProcessPayment(
                razorpay_order_id, razorpay_payment_id, razorpay_signature);

        return isValid ? "success" : "failure";
    }

    @GetMapping("/payment-history")
    public String showPaymentHistory(Model model, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<PaymentTransaction> transactions = paymentService.getUserPaymentHistory(userId);
        model.addAttribute("payments", transactions);
        return "payment-history";
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}