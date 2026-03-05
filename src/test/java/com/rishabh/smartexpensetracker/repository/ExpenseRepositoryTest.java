package com.rishabh.smartexpensetracker.repository;

import com.rishabh.smartexpensetracker.entity.Expense;
import com.rishabh.smartexpensetracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=YEAR,MONTH",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "spring.task.scheduling.enabled=false"
})
@Transactional
class ExpenseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("User")
                .email("user@test.com")
                .password("secret")
                .createdAt(LocalDateTime.now())
                .build());

        expenseRepository.save(Expense.builder()
                .user(user)
                .category("Food")
                .description("Lunch")
                .amount(100.0)
                .date(LocalDate.of(2026, 3, 1))
                .build());

        expenseRepository.save(Expense.builder()
                .user(user)
                .category("Travel")
                .description("Taxi")
                .amount(200.0)
                .date(LocalDate.of(2026, 3, 5))
                .build());

        expenseRepository.save(Expense.builder()
                .user(user)
                .category("Food")
                .description("Dinner")
                .amount(50.0)
                .date(LocalDate.of(2026, 4, 1))
                .build());
    }

    @Test
    void findByUserId_returnsAllUserExpenses() {
        List<Expense> result = expenseRepository.findByUserId(user.getId());

        assertEquals(3, result.size());
    }

    @Test
    void findMonthlyExpenses_filtersByMonthAndYear() {
        List<Expense> result = expenseRepository.findMonthlyExpenses(user.getId(), 3, 2026);

        assertEquals(2, result.size());
    }

    @Test
    void getCategorySummary_returnsGroupedTotals() {
        List<Object[]> result = expenseRepository.getCategorySummary(user.getId(), 2026, 3);

        assertEquals(2, result.size());

        double foodTotal = result.stream()
                .filter(row -> "Food".equals(row[0]))
                .map(row -> (Double) row[1])
                .findFirst()
                .orElse(0.0);

        assertEquals(100.0, foodTotal);
    }

    @Test
    void getTotalExpenseForMonth_returnsSum() {
        Double total = expenseRepository.getTotalExpenseForMonth(user.getId(), 2026, 3);

        assertEquals(300.0, total);
    }
}
