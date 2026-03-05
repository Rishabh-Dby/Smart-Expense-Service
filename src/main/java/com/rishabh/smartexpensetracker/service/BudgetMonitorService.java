package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetMonitorService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkBudgetsDaily() {

        List<User> users = userRepository.findAll();

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        for (User user : users) {

            Optional<Budget> budgetOpt =
                    budgetRepository.findByUserIdAndYearAndMonth(
                            user.getId(),
                            year,
                            month
                    );

            if (budgetOpt.isEmpty()) {
                continue;
            }

            Budget budget = budgetOpt.get();

            Double totalExpense =
                    expenseRepository.getTotalExpenseForMonth(
                            user.getId(),
                            year,
                            month
                    );

            if (totalExpense != null &&
                    totalExpense > budget.getLimitAmount()) {

                emailService.sendBudgetExceededEmail(
                        user.getEmail(),
                        totalExpense,
                        budget.getLimitAmount()
                );
            }
        }
    }
}