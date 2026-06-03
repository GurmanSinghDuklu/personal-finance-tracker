package com.gurman.financetracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A simple data carrier (DTO) returned by the summary endpoint.
 * It is not a MongoDB document — it's only used to shape the JSON response.
 *
 * Using a dedicated class here (instead of a Map) makes the API contract explicit
 * and gives the caller a predictable, typed response body.
 */
@Data
@AllArgsConstructor
public class SummaryResponse {

    /** Sum of all INCOME transactions. */
    private double totalIncome;

    /** Sum of all EXPENSE transactions. */
    private double totalExpenses;

    /** totalIncome minus totalExpenses. Positive = net profit, negative = net loss. */
    private double netBalance;
}
