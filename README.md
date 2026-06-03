# Personal Finance Tracker API

A RESTful API built with Java 17 and Spring Boot 3 for tracking personal income and expenses. Built as a portfolio project demonstrating clean layered architecture, input validation, and error handling.

## Tech Stack

- **Java 17**
- **Spring Boot 3.3**
- **Spring Data MongoDB**
- **Lombok**
- **Spring Validation**
- **Maven**

## Project Structure

```
src/main/java/com/gurman/financetracker/
├── FinanceTrackerApplication.java       # Entry point
├── model/
│   ├── Transaction.java                 # MongoDB document model
│   ├── TransactionType.java             # INCOME / EXPENSE enum
│   └── SummaryResponse.java             # DTO for summary endpoint
├── repository/
│   └── TransactionRepository.java       # MongoDB data access
├── service/
│   └── TransactionService.java          # Business logic
├── controller/
│   └── TransactionController.java       # HTTP endpoints
└── exception/
    ├── GlobalExceptionHandler.java      # Centralised error handling
    └── TransactionNotFoundException.java
```

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB running locally on port 27017

**Install MongoDB (macOS):**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

## Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/transactions` | Get all transactions |
| `GET` | `/api/transactions/{id}` | Get transaction by ID |
| `GET` | `/api/transactions/category/{category}` | Filter by category |
| `GET` | `/api/transactions/summary` | Get income/expense summary |
| `POST` | `/api/transactions` | Create a new transaction |
| `PUT` | `/api/transactions/{id}` | Update a transaction |
| `DELETE` | `/api/transactions/{id}` | Delete a transaction |

## Sample Requests

### Create a transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Monthly rent payment",
    "amount": 850.00,
    "type": "EXPENSE",
    "category": "rent",
    "date": "2024-06-01"
  }'
```

### Get all transactions

```bash
curl http://localhost:8080/api/transactions
```

### Get summary

```bash
curl http://localhost:8080/api/transactions/summary
```

**Response:**
```json
{
  "totalIncome": 3000.00,
  "totalExpenses": 1200.00,
  "netBalance": 1800.00
}
```

### Filter by category

```bash
curl http://localhost:8080/api/transactions/category/food
```

## Error Responses

All errors return a consistent JSON shape:

```json
{
  "timestamp": "2024-06-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Transaction not found with id: abc123"
}
```

Validation errors include per-field detail:

```json
{
  "timestamp": "2024-06-01T10:30:00",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "amount": "Amount must be a positive number",
    "description": "Description must not be blank"
  }
}
```

## Architecture

```
HTTP Request
    ↓
Controller  — maps HTTP verbs/paths, returns ResponseEntity
    ↓
Service     — business logic, orchestrates data flow
    ↓
Repository  — MongoDB queries via Spring Data
    ↓
MongoDB
```

## Connecting to MongoDB Atlas (Cloud)

In `application.properties`, comment out the local config and uncomment:

```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/finance_tracker?retryWrites=true&w=majority
```
