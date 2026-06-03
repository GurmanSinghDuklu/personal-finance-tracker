package com.gurman.financetracker.model;

/**
 * Represents whether a transaction adds money (INCOME) or removes money (EXPENSE).
 * Kept as a separate file so it can be reused across the codebase without importing Transaction.
 */
public enum TransactionType {
    INCOME,
    EXPENSE
}
