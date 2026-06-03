package com.gurman.financetracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * The core data model for the application.
 *
 * @Document("transactions") tells Spring Data MongoDB to store these objects
 * in a MongoDB collection called "transactions".
 *
 * Lombok annotations:
 *   @Data         — generates getters, setters, equals, hashCode, toString
 *   @Builder      — lets you create objects with builder pattern: Transaction.builder().amount(50.0).build()
 *   @NoArgsConstructor / @AllArgsConstructor — generates constructors (both needed when using @Builder)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    /** MongoDB's unique identifier for each document. Spring sets this automatically on save. */
    @Id
    private String id;

    /** A human-readable label, e.g. "Monthly rent payment". Must not be blank. */
    @NotBlank(message = "Description must not be blank")
    private String description;

    /**
     * The monetary value. Must be a positive number (> 0).
     * Type is not income/expense — that's determined by the 'type' field below.
     */
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive number")
    private Double amount;

    /** INCOME or EXPENSE — determines how the amount affects the balance. */
    @NotNull(message = "Transaction type is required (INCOME or EXPENSE)")
    private TransactionType type;

    /** A free-text label to group transactions: "food", "rent", "salary", etc. */
    @NotBlank(message = "Category must not be blank")
    private String category;

    /** The date the transaction occurred. Format: YYYY-MM-DD (e.g. 2024-06-15). */
    @NotNull(message = "Date is required")
    private LocalDate date;
}
