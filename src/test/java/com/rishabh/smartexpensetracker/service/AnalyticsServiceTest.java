package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.dto.MonthlyAnalyticsResponse;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.ExpenseRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getMonthlyAnalytics_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> analyticsService.getMonthlyAnalytics("missing@test.com", 2026, 3));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void getMonthlyAnalytics_returnsComputedSummary() {
        User user = User.builder().id(10L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(expenseRepository.getCategorySummary(10L, 2026, 3)).thenReturn(List.of(
                new Object[]{"Food", 1200.0},
                new Object[]{"Travel", 1800.0}
        ));

        MonthlyAnalyticsResponse result = analyticsService.getMonthlyAnalytics("user@test.com", 2026, 3);

        assertEquals(3000.0, result.getTotalSpending());
        assertEquals("Travel", result.getHighestCategory());
        assertEquals(2, result.getCategoryBreakdown().size());
        assertEquals(1200.0, result.getCategoryBreakdown().get("Food"));
    }
}
