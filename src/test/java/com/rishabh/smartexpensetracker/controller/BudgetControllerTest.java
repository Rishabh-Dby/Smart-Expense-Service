package com.rishabh.smartexpensetracker.controller;

import com.rishabh.smartexpensetracker.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BudgetController(budgetService)).build();
    }

    @Test
    void setBudget_returnsSuccessMessage() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken("user@test.com", "pass");

        mockMvc.perform(post("/budget")
                        .param("year", "2026")
                        .param("month", "3")
                        .param("limit", "10000")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(content().string("Budget set successfully"));

        verify(budgetService).setBudget("user@test.com", 2026, 3, 10000.0);
    }
}
