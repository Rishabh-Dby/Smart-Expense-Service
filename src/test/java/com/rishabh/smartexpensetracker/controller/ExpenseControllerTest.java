package com.rishabh.smartexpensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishabh.smartexpensetracker.dto.CreateExpenseRequest;
import com.rishabh.smartexpensetracker.entity.Expense;
import com.rishabh.smartexpensetracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExpenseController(expenseService)).build();
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void addExpense_returnsSuccessMessage() throws Exception {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(200.0);
        request.setCategory("Food");
        request.setDescription("Lunch");
        request.setDate(LocalDate.of(2026, 3, 1));

        Authentication auth = new UsernamePasswordAuthenticationToken("user@test.com", "pass");

        mockMvc.perform(post("/expenses")
                        .principal(auth)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Expense added successfully"));

        verify(expenseService).addExpense(any(CreateExpenseRequest.class), org.mockito.Mockito.eq("user@test.com"));
    }

    @Test
    void getMyExpenses_returnsExpenseList() throws Exception {
        Expense expense = Expense.builder().id(1L).category("Food").amount(150.0).build();
        when(expenseService.getUserExpenses("user@test.com")).thenReturn(List.of(expense));

        Authentication auth = new UsernamePasswordAuthenticationToken("user@test.com", "pass");

        mockMvc.perform(get("/expenses").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].category").value("Food"));

        verify(expenseService).getUserExpenses("user@test.com");
    }

    @Test
    void uploadExpenses_returnsSuccessMessage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "expenses.csv",
                "text/csv",
                "date,amount,category,description\n2026-03-01,100,Food,Lunch".getBytes(StandardCharsets.UTF_8)
        );

        Authentication auth = new UsernamePasswordAuthenticationToken("user@test.com", "pass");

        mockMvc.perform(multipart("/expenses/upload")
                        .file(file)
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(content().string("Expenses uploaded successfully"));

        verify(expenseService).importCsv(any(), org.mockito.Mockito.eq("user@test.com"));
    }
}
