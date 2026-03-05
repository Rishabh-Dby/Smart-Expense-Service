package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.BudgetRepository;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void setBudget_throwsWhenUserMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> budgetService.setBudget("missing@test.com", 2026, 3, 1000.0));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void setBudget_createsNewBudgetWhenNotFound() {
        User user = User.builder().id(1L).email("user@test.com").build();
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, 2026, 3)).thenReturn(Optional.empty());

        budgetService.setBudget("user@test.com", 2026, 3, 1000.0);

        ArgumentCaptor<Budget> captor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(captor.capture());

        Budget saved = captor.getValue();
        assertEquals(user, saved.getUser());
        assertEquals(2026, saved.getYear());
        assertEquals(3, saved.getMonth());
        assertEquals(1000.0, saved.getLimitAmount());
    }

    @Test
    void setBudget_updatesExistingBudget() {
        User user = User.builder().id(1L).email("user@test.com").build();
        Budget existing = Budget.builder().id(10L).user(user).year(2026).month(3).limitAmount(500.0).build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserIdAndYearAndMonth(1L, 2026, 3)).thenReturn(Optional.of(existing));

        budgetService.setBudget("user@test.com", 2026, 3, 1200.0);

        verify(budgetRepository).save(existing);
        assertEquals(1200.0, existing.getLimitAmount());
    }
}
