package com.gurman.financetracker.service;

import com.gurman.financetracker.exception.TransactionNotFoundException;
import com.gurman.financetracker.model.SummaryResponse;
import com.gurman.financetracker.model.Transaction;
import com.gurman.financetracker.model.TransactionType;
import com.gurman.financetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service layer contains all business logic.
 *
 * Its job is to:
 *   1. Coordinate between the controller and the repository
 *   2. Apply any rules (e.g. "a transaction must exist before it can be updated")
 *   3. Keep business logic OUT of the controller and OUT of the repository
 *
 * This separation means each layer has one responsibility — a core principle
 * of clean, maintainable code.
 */
@Service
public class TransactionService {

    private final TransactionRepository repository;

    /**
     * Constructor injection: Spring automatically injects the repository here.
     * This is preferred over @Autowired field injection because:
     *   - The dependency is explicit and required (can't forget to set it)
     *   - Makes unit testing easier (you can pass a mock in the constructor)
     *   - Lombok's @RequiredArgsConstructor can generate this, but explicit is clearer for learning
     */
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    /** Returns every transaction in the database. */
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    /**
     * Returns a single transaction by its MongoDB ID.
     * Throws TransactionNotFoundException (→ 404) if not found.
     */
    public Transaction getTransactionById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    /** Returns all transactions that belong to the given category. */
    public List<Transaction> getTransactionsByCategory(String category) {
        return repository.findByCategory(category);
    }

    /**
     * Saves a new transaction to the database.
     * The @Id field is null when passed in; MongoDB assigns a unique ID on save.
     */
    public Transaction createTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    /**
     * Replaces an existing transaction with new data.
     * We copy the ID from the URL into the request body object so MongoDB
     * updates the existing document rather than creating a new one.
     */
    public Transaction updateTransaction(String id, Transaction updatedTransaction) {
        // Verify the transaction exists first — throws 404 if not
        getTransactionById(id);

        // Carry the existing ID into the updated object so .save() performs an update
        updatedTransaction.setId(id);
        return repository.save(updatedTransaction);
    }

    /**
     * Removes a transaction by ID.
     * Verifies existence first so a DELETE on a missing ID returns 404 rather than silently succeeding.
     */
    public void deleteTransaction(String id) {
        getTransactionById(id); // throws 404 if not found
        repository.deleteById(id);
    }

    /**
     * Calculates a financial summary across all transactions.
     *
     * Uses Java streams to filter and sum in one pass each:
     *   - filter(INCOME) → sum amounts → totalIncome
     *   - filter(EXPENSE) → sum amounts → totalExpenses
     *   - netBalance = totalIncome - totalExpenses
     */
    public SummaryResponse getSummary() {
        List<Transaction> all = repository.findAll();

        double totalIncome = all.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = all.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double netBalance = totalIncome - totalExpenses;

        return new SummaryResponse(totalIncome, totalExpenses, netBalance);
    }
}
