package com.rishabh.smartexpensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendBudgetExceededEmail(
            String to,
            Double total,
            Double limit) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@expensetracker.app");
        message.setTo(to);
        message.setSubject("Budget Exceeded Alert 🚨");
        message.setText(
                "You have exceeded your monthly budget.\n\n" +
                        "Budget Limit: ₹" + limit + "\n" +
                        "Current Spending: ₹" + total
        );

        mailSender.send(message);
    }
}
