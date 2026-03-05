package com.rishabh.smartexpensetracker.controller;

import com.rishabh.smartexpensetracker.dto.MonthlyAnalyticsResponse;
import com.rishabh.smartexpensetracker.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AnalyticsController(analyticsService)).build();
    }

    @Test
    void getMonthlyAnalytics_returnsResponse() throws Exception {
        MonthlyAnalyticsResponse response = new MonthlyAnalyticsResponse(5000.0, "Food", Map.of("Food", 5000.0));
        when(analyticsService.getMonthlyAnalytics("user@test.com", 2026, 3)).thenReturn(response);

        Authentication auth = new UsernamePasswordAuthenticationToken("user@test.com", "pass");

        mockMvc.perform(get("/analytics/monthly")
                        .param("year", "2026")
                        .param("month", "3")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSpending").value(5000.0))
                .andExpect(jsonPath("$.highestCategory").value("Food"));

        verify(analyticsService).getMonthlyAnalytics("user@test.com", 2026, 3);
    }
}
