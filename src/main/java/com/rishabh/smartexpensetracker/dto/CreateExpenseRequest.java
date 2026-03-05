package com.rishabh.smartexpensetracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateExpenseRequest {

    private Double amount;
    private String description;
    private String category;
    private LocalDate date;
}
