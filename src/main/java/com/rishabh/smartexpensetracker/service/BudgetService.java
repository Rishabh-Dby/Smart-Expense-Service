package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public void setBudget(
            String email,
            int year,
            int month,
            Double limitAmount) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository
                .findByUserIdAndYearAndMonth(
                        user.getId(),
                        year,
                        month
                )
                .orElse(new Budget());

        budget.setUser(user);
        budget.setYear(year);
        budget.setMonth(month);
        budget.setLimitAmount(limitAmount);

        budgetRepository.save(budget);
    }
}
