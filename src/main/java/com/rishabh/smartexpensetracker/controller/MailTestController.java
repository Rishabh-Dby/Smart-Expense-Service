package com.rishabh.smartexpensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final JavaMailSender mailSender;

    @GetMapping("/test-mail")
    public String sendMail() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@expensetracker.app");
        message.setTo("test@test.com");
        message.setSubject("Test Mail");
        message.setText("Mailtrap working");

        mailSender.send(message);

        return "Mail sent";
    }
}