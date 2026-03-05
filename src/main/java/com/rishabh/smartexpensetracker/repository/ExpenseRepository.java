package com.rishabh.smartexpensetracker.repository;

import com.rishabh.smartexpensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Expense> findMonthlyExpenses(Long userId, int month, int year);

    @Query("""
       SELECT e.category, SUM(e.amount)
       FROM Expense e
       WHERE e.user.id = :userId
       AND EXTRACT(YEAR FROM e.date) = :year
       AND EXTRACT(MONTH FROM e.date) = :month
       GROUP BY e.category
       """)
    List<Object[]> getCategorySummary(Long userId, int year, int month);

    @Query("""
       SELECT SUM(e.amount)
       FROM Expense e
       WHERE e.user.id = :userId
       AND YEAR(e.date) = :year
       AND MONTH(e.date) = :month
       """)
    Double getTotalExpenseForMonth(Long userId, int year, int month);
}
