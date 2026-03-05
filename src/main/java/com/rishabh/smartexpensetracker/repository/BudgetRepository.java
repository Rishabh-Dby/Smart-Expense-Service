package com.rishabh.smartexpensetracker.repository;

import com.rishabh.smartexpensetracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserIdAndYearAndMonth(
            Long userId,
            int year,
            int month
    );
}
