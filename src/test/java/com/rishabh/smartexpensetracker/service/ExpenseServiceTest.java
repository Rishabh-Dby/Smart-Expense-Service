package com.rishabh.smartexpensetracker.service;

import com.opencsv.exceptions.CsvException;
import com.rishabh.smartexpensetracker.dto.CreateExpenseRequest;
import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.Expense;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void addExpense_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setDate(LocalDate.of(2026, 3, 1));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> expenseService.addExpense(request, "missing@test.com"));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void addExpense_sendsEmailWhenBudgetExceeded() {
        User user = User.builder().id(1L).email("user@test.com").build();
        Budget budget = Budget.builder().id(10L).limitAmount(1000.0).build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(expenseRepository.getCategorySummary(1L, 2026, 3)).thenReturn(List.of(
                new Object[]{"Food", 700.0},
                new Object[]{"Travel", 500.0}
        ));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, 2026, 3)).thenReturn(Optional.of(budget));

        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(200.0);
        request.setDescription("Lunch");
        request.setCategory("Food");
        request.setDate(LocalDate.of(2026, 3, 5));

        expenseService.addExpense(request, "user@test.com");

        verify(expenseRepository).save(any(Expense.class));
        verify(emailService).sendBudgetExceededEmail("user@test.com", 1200.0, 1000.0);
    }

    @Test
    void addExpense_doesNotSendEmailWhenWithinBudget() {
        User user = User.builder().id(1L).email("user@test.com").build();
        Budget budget = Budget.builder().id(10L).limitAmount(2000.0).build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(expenseRepository.getCategorySummary(1L, 2026, 3)).thenReturn(List.<Object[]>of(new Object[]{"Food", 1200.0}));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, 2026, 3)).thenReturn(Optional.of(budget));

        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setDescription("Lunch");
        request.setCategory("Food");
        request.setDate(LocalDate.of(2026, 3, 5));

        expenseService.addExpense(request, "user@test.com");

        verify(emailService, never()).sendBudgetExceededEmail(any(), any(), any());
    }

    @Test
    void getUserExpenses_returnsExpenses() {
        User user = User.builder().id(1L).email("user@test.com").build();
        Expense expense = Expense.builder().id(7L).category("Food").amount(50.0).build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(expenseRepository.findByUserId(1L)).thenReturn(List.of(expense));

        List<Expense> result = expenseService.getUserExpenses("user@test.com");

        assertEquals(1, result.size());
        assertEquals(7L, result.get(0).getId());
    }

    @Test
    void importCsv_parsesAndSavesRows() throws IOException, CsvException {
        User user = User.builder().id(1L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        String csv = "date,amount,category,description\n2026-03-01,100.50,Food,Lunch\n2026-03-02,300.00,Travel,Taxi";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "expenses.csv",
                "text/csv",
                csv.getBytes(StandardCharsets.UTF_8)
        );

        expenseService.importCsv(file, "user@test.com");

        ArgumentCaptor<List<Expense>> captor = ArgumentCaptor.forClass(List.class);
        verify(expenseRepository).saveAll(captor.capture());

        List<Expense> saved = captor.getValue();
        assertEquals(2, saved.size());
        assertEquals("Food", saved.get(0).getCategory());
        assertEquals(300.0, saved.get(1).getAmount());
    }

    @Test
    void importCsv_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());
        MockMultipartFile file = new MockMultipartFile("file", "x.csv", "text/csv", "date,amount,category,description".getBytes(StandardCharsets.UTF_8));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> expenseService.importCsv(file, "missing@test.com"));

        assertEquals("User not found", ex.getMessage());
        verify(expenseRepository, never()).saveAll(anyList());
    }
}
