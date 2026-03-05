package com.rishabh.smartexpensetracker.controller;

import com.rishabh.smartexpensetracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<String> setBudget(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam Double limit,
            Authentication authentication) {

        String email = authentication.getName();

        budgetService.setBudget(email, year, month, limit);

        return ResponseEntity.ok("Budget set successfully");
    }
}
