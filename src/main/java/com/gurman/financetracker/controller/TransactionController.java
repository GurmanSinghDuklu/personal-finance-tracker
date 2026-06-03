package com.gurman.financetracker.controller;

import com.gurman.financetracker.model.SummaryResponse;
import com.gurman.financetracker.model.Transaction;
import com.gurman.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller layer is the HTTP entry point for the application.
 *
 * Its responsibilities:
 *   1. Map incoming HTTP requests to service method calls
 *   2. Return appropriate HTTP status codes and response bodies
 *   3. NOT contain any business logic — that belongs in the service
 *
 * @RestController   = @Controller + @ResponseBody: every method returns JSON automatically
 * @RequestMapping   sets the base URL path for all endpoints in this class
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    /** Constructor injection — see TransactionService for why this is preferred over @Autowired */
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * GET /api/transactions
     * Returns all transactions with HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    /**
     * GET /api/transactions/summary
     *
     * IMPORTANT: This mapping must be declared BEFORE /{id} to avoid Spring
     * treating "summary" as a path variable value.
     *
     * Returns total income, total expenses, and net balance.
     */
    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary() {
        return ResponseEntity.ok(service.getSummary());
    }

    /**
     * GET /api/transactions/{id}
     *
     * @PathVariable extracts the {id} segment from the URL.
     * Returns 200 OK if found, or 404 Not Found (handled by GlobalExceptionHandler).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(service.getTransactionById(id));
    }

    /**
     * GET /api/transactions/category/{category}
     * e.g. GET /api/transactions/category/food
     * Returns all transactions in the given category.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Transaction>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getTransactionsByCategory(category));
    }

    /**
     * POST /api/transactions
     *
     * @RequestBody   — Spring deserialises the JSON body into a Transaction object
     * @Valid         — triggers validation of all @NotBlank, @Positive etc. annotations on the model
     *
     * Returns 201 Created (not 200) because a new resource was created.
     * ResponseEntity.status(HttpStatus.CREATED).body(...) sets the status code explicitly.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction created = service.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/transactions/{id}
     * Replaces the entire transaction document with the provided body.
     * Returns 200 OK with the updated transaction.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable String id,
            @Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(service.updateTransaction(id, transaction));
    }

    /**
     * DELETE /api/transactions/{id}
     * Returns 204 No Content on success — the standard response when there is no body to return.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
