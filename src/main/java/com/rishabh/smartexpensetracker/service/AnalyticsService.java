package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.dto.MonthlyAnalyticsResponse;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Cacheable(
            value = "monthlyAnalytics",
            key = "#email + '-' + #year + '-' + #month"
    )
    public MonthlyAnalyticsResponse getMonthlyAnalytics(
            String email,
            int year,
            int month) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Object[]> results =
                expenseRepository.getCategorySummary(
                        user.getId(),
                        year,
                        month
                );

        Map<String, Double> categoryMap = new HashMap<>();
        double total = 0;
        String highestCategory = null;
        double maxAmount = 0;

        for (Object[] row : results) {
            String category = (String) row[0];
            Double amount = (Double) row[1];

            categoryMap.put(category, amount);
            total += amount;

            if (amount > maxAmount) {
                maxAmount = amount;
                highestCategory = category;
            }
        }

        return new MonthlyAnalyticsResponse(
                total,
                highestCategory,
                categoryMap
        );
    }
}
