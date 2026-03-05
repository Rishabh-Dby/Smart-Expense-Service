package com.rishabh.smartexpensetracker.controller;

import com.rishabh.smartexpensetracker.dto.MonthlyAnalyticsResponse;
import com.rishabh.smartexpensetracker.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyAnalyticsResponse> getMonthlyAnalytics(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                analyticsService.getMonthlyAnalytics(
                        email,
                        year,
                        month
                )
        );
    }
}
