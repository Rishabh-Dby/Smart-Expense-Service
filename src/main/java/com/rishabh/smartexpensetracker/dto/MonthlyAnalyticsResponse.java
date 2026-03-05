package com.rishabh.smartexpensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MonthlyAnalyticsResponse {

    private Double totalSpending;
    private String highestCategory;
    private Map<String, Double> categoryBreakdown;
}
