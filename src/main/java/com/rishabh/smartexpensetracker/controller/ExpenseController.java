package com.rishabh.smartexpensetracker.controller;

import com.opencsv.exceptions.CsvException;
import com.rishabh.smartexpensetracker.dto.CreateExpenseRequest;
import com.rishabh.smartexpensetracker.entity.Expense;
import com.rishabh.smartexpensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<String> addExpense(
            @RequestBody CreateExpenseRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        expenseService.addExpense(request, email);

        return ResponseEntity.ok("Expense added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getMyExpenses(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                expenseService.getUserExpenses(email)
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExpenses(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException, CsvException {

        String email = authentication.getName();

        expenseService.importCsv(file, email);

        return ResponseEntity.ok("Expenses uploaded successfully");
    }
}
