package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetMonitorServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BudgetMonitorService budgetMonitorService;

    @Test
    void checkBudgetsDaily_sendsEmailWhenBudgetExceeded() {
        LocalDate now = LocalDate.now();

        User user = User.builder().id(1L).email("user@test.com").build();
        Budget budget = Budget.builder().id(10L).user(user).year(now.getYear()).month(now.getMonthValue()).limitAmount(1000.0).build();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, now.getYear(), now.getMonthValue()))
                .thenReturn(Optional.of(budget));
        when(expenseRepository.getTotalExpenseForMonth(1L, now.getYear(), now.getMonthValue())).thenReturn(1200.0);

        budgetMonitorService.checkBudgetsDaily();

        verify(emailService).sendBudgetExceededEmail("user@test.com", 1200.0, 1000.0);
    }

    @Test
    void checkBudgetsDaily_skipsWhenNoBudgetOrNotExceeded() {
        LocalDate now = LocalDate.now();

        User first = User.builder().id(1L).email("first@test.com").build();
        User second = User.builder().id(2L).email("second@test.com").build();
        Budget budget = Budget.builder().id(10L).user(second).year(now.getYear()).month(now.getMonthValue()).limitAmount(1000.0).build();

        when(userRepository.findAll()).thenReturn(List.of(first, second));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, now.getYear(), now.getMonthValue()))
                .thenReturn(Optional.empty());
        when(budgetRepository.findByUserIdAndYearAndMonth(2L, now.getYear(), now.getMonthValue()))
                .thenReturn(Optional.of(budget));
        when(expenseRepository.getTotalExpenseForMonth(2L, now.getYear(), now.getMonthValue())).thenReturn(900.0);

        budgetMonitorService.checkBudgetsDaily();

        verify(emailService, never()).sendBudgetExceededEmail(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyDouble(), org.mockito.ArgumentMatchers.anyDouble());
    }
}
