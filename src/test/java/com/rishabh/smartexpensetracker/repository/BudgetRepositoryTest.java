package com.rishabh.smartexpensetracker.repository;

import com.rishabh.smartexpensetracker.entity.Budget;
import com.rishabh.smartexpensetracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class BudgetRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    void findByUserIdAndYearAndMonth_returnsBudget() {
        User user = userRepository.save(User.builder()
                .name("User")
                .email("user@test.com")
                .password("secret")
                .createdAt(LocalDateTime.now())
                .build());

        budgetRepository.save(Budget.builder()
                .user(user)
                .year(2026)
                .month(3)
                .limitAmount(1000.0)
                .build());

        Optional<Budget> result = budgetRepository.findByUserIdAndYearAndMonth(user.getId(), 2026, 3);

        assertTrue(result.isPresent());
        assertEquals(1000.0, result.get().getLimitAmount());
    }
}
