package com.gurman.financetracker.repository;

import com.gurman.financetracker.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository layer is responsible for all database communication.
 *
 * By extending MongoRepository<Transaction, String>, Spring Data automatically provides:
 *   - findAll()         — fetch every document in the collection
 *   - findById(id)      — fetch one document by its _id
 *   - save(entity)      — insert a new document or update an existing one
 *   - deleteById(id)    — remove a document by its _id
 *   - existsById(id)    — check whether a document exists
 *
 * You don't write any SQL or MongoDB queries for standard operations — Spring generates them.
 *
 * The second generic type <String> is the type of the @Id field in Transaction.
 */
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Spring Data derives the query from the method name automatically.
     * "findBy" + "Category" generates: db.transactions.find({ category: <value> })
     * No implementation needed — Spring writes it at runtime.
     *
     * @param category the category string to filter by (case-sensitive)
     * @return all transactions matching that category
     */
    List<Transaction> findByCategory(String category);
}
