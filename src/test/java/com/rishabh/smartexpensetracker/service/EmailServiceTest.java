package com.rishabh.smartexpensetracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendBudgetExceededEmail_sendsExpectedMessage() {
        emailService.sendBudgetExceededEmail("user@test.com", 1200.0, 1000.0);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mail = captor.getValue();
        assertEquals("noreply@expensetracker.app", mail.getFrom());
        assertEquals("user@test.com", mail.getTo()[0]);
        assertTrue(mail.getSubject().contains("Budget Exceeded Alert"));
        assertTrue(mail.getText().contains("1000.0"));
        assertTrue(mail.getText().contains("1200.0"));
    }
}
