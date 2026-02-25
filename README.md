# aggregate-categorize-service

The **Aggregate & Categorize Service** is responsible for retrieving, aggregating, and categorizing client transactions across multiple banking data sources.

This service acts as an orchestration layer between downstream services and provides a unified, categorized transaction view for a given customer.

---

## Overview

The service accepts a **Customer Info File (CIF) Key** representing a Kapitek client and performs the following:

1. Retrieves customer profile information.
2. Identifies all linked accounts and standalone cards.
3. Fetches transactions from respective services.
4. Aggregates transactions into a single dataset.
5. Categorizes transactions.
6. Persists categorized results in an H2 database.
7. Returns categorized transactions based on a requested time range.

---

## Architecture Flow

### Step 1: Retrieve Customer Information
The service calls the **Customer Info File (CIF) Service** using the provided CIF key to obtain:
- Account numbers (Cheque, Savings, etc.)
- Standalone cards (Credit Card, Prepaid Card)

### Step 2: Fetch Account Transactions
For each account:
- Calls the **accounts-service**
- Retrieves all transactions linked to that account

If multiple accounts exist, multiple calls are made.

### Step 3: Fetch Card Transactions
For each standalone card:
- Calls the **cards-service**
- Retrieves all transactions linked to that card

### Step 4: Aggregate Transactions
All account and card transactions are combined into a single unified dataset.

### Step 5: Categorization
The service applies categorization logic to classify transactions (e.g., groceries, utilities, entertainment, etc.).

### Step 6: Persistence
Categorized transactions are stored in an **H2 in-memory database**.

---

## API Request Requirements

The service requires:

- **Customer Info File (CIF) Key**
- **Months Parameter**

The `months` parameter determines how far back the transaction history should be retrieved.

### Example
If `months = 3`, the service returns categorized transactions from the past 3 months.

---

## Security

### Service-to-Service Authentication
The service authenticates with:
- `accounts-service`
- `cards-service`
- `cif-service`

Authentication is performed using client credentials configured on the API gateway.

### Keycloak Integration
The aggregate service is protected by its own **Keycloak realm**.

- Required Role: `TRANSACTION_AGGREGATE_API_ACCESS`
- A test client named `mobile-app` has been created.
- The `mobile-app` client has:
  - Service account enabled
  - Required role assigned

Only clients with the correct role can access the aggregation endpoints.

---

## Data Storage

- Database: **H2 (In-Memory)**
- Stores categorized transaction results
- Intended for development and testing purposes

---

## Purpose

This service enables:

- Unified transaction views
- Cross-product transaction aggregation
- Categorized financial insights
- Simplified consumption for frontend or mobile applications

---

## Future Improvements

- Replace H2 with a production-grade database (e.g., PostgreSQL)
- Improve categorization rules using ML models
- Add pagination and filtering capabilities
- Introduce caching for performance optimization
