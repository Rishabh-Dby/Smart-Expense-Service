package com.rishabh.smartexpensetracker.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.rishabh.smartexpensetracker.dto.CreateExpenseRequest;
import com.rishabh.smartexpensetracker.entity.Expense;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    private final BudgetRepository budgetRepository;
    private final EmailService emailService;

    @CacheEvict(value = "monthlyAnalytics", allEntries = true)
    public void addExpense(CreateExpenseRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .category(request.getCategory())
                .date(request.getDate())
                .user(user)
                .build();

        expenseRepository.save(expense);

        // 🔥 Budget Check
        int year = request.getDate().getYear();
        int month = request.getDate().getMonthValue();

        Double totalSpending = expenseRepository
                .getCategorySummary(user.getId(), year, month)
                .stream()
                .map(row -> (Double) row[1])
                .reduce(0.0, Double::sum);

        budgetRepository
                .findByUserIdAndYearAndMonth(
                        user.getId(),
                        year,
                        month
                )
                .ifPresent(budget -> {

                    if (totalSpending > budget.getLimitAmount()) {

                        emailService.sendBudgetExceededEmail(
                                user.getEmail(),
                                totalSpending,
                                budget.getLimitAmount()
                        );
                    }
                });
    }

    public List<Expense> getUserExpenses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseRepository.findByUserId(user.getId());
    }

    public void importCsv(MultipartFile file, String email) throws IOException, CsvException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> expenses = new ArrayList<>();

        CSVReader reader = new CSVReader(
                new InputStreamReader(file.getInputStream())
        );

        List<String[]> rows = reader.readAll();

        for (int i = 1; i < rows.size(); i++) { // skip header

            String[] row = rows.get(i);

            LocalDate date = LocalDate.parse(row[0]);
            Double amount = Double.parseDouble(row[1]);
            String category = row[2];
            String description = row[3];

            Expense expense = Expense.builder()
                    .date(date)
                    .amount(amount)
                    .category(category)
                    .description(description)
                    .user(user)
                    .build();

            expenses.add(expense);
        }

        expenseRepository.saveAll(expenses);
    }
}
