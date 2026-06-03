package com.gurman.financetracker.exception;

/**
 * Thrown by the service layer when a transaction ID does not exist in the database.
 * The GlobalExceptionHandler catches this and converts it into a 404 HTTP response.
 *
 * Extending RuntimeException means callers don't need to declare it in a throws clause,
 * which keeps service method signatures clean.
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String id) {
        super("Transaction not found with id: " + id);
    }
}
