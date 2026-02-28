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


## Setup To Run The Application

### Docker Compose Setup

The **kapitek-transaction-aggregate-service** is not a standalone service.  
It depends on multiple external services that must run together for the system to function correctly.

### Service Dependencies

- **kapitek-vendor-mock-service**
  - CIF (Customer Info File) service  
  - Accounts service  
  - Cards service  

- **Keycloak**
  - Used for JWT authentication and token validation  
  - Runs in **PROD mode**, therefore requires its own PostgreSQL database  

- **PostgreSQL**
  - Database for Keycloak  
  - Database for mock services  

- **Vault**
  - Manages and protects secrets used by:
    - aggregate-service  
    - mock services  

All required dependencies are configured in the `docker-compose.yml` file located in the root directory of the **kapitek-transaction-aggregate-service** Spring Boot application.

---

### ✅ Prerequisites

Make sure you have:

- Docker installed
- Docker Compose installed

---

### ▶️ Start The Environment

From the root directory of the project, run:

```bash
docker compose up -d
```

Or from another location:

```bash
docker compose -f <path-to-docker-compose>/docker-compose.yml up -d
```

Docker will:

- Pull the required images  
- Build containers  
- Start services in dependency order  

Once completed, all services will be running and ready for testing.

---

## 🧪 Testing The Application

Comprehensive Swagger (OpenAPI) documentation is available for:

- **kapitek-transaction-aggregate-service**
- **kapitek-vendor-mock-service**

---

## 🏦 Step 1 — Create a Mock Customer (CIF)

Before performing transaction aggregation, a customer must exist in **Kapitek Bank**.

Use the CIF service to create a mock customer.

### **Create Mock Customer Request**

```bash
curl --location 'http://localhost:8081/kapitek-cif/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <TOKEN>' \
--data '{
  "identityNumber": "9611270905081",
  "isChequesAccountRequired": "YES",
  "isSavingsAccountRequired": "NO",
  "isCreditCardRequired": "YES",
  "isPrepaidCardRequired": "NO"
}'
```

### **Create Mock Customer Response**

```json
{
  "customerInfoFileKey": "d68b5116-1cd8-4da5-a401-017538f0ec3a",
  "identityNumber": "9611270905081",
  "accounts": {
    "chequeAccountNumber": "3376734877",
    "savingsAccountNumber": null
  },
  "cards": {
    "creditCardNumber": "2165178172740537",
    "prePaidCardNumber": null
  }
}
```

> ⚠️ Save the `customerInfoFileKey`. It is required for transaction aggregation.

---

## 💳 Step 2 — Add Mock Transactions

After creating the customer, generate mock transactions for accounts and/or cards.

### **Add Account Mock Transactions Request**

```bash
curl --location 'http://localhost:8081/kapitek-account/transactions/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <TOKEN>' \
--data '{
  "accountNumber": "3376734877",
  "totalNumberOfOutGoingTransactions": 2,
  "totalNumberOfIncomingTransactions": 2,
  "rangeBetweenStartDate": "2024-01-01T00:00:00",
  "rangeBetweenEndDate": "2026-02-24T23:59:59"
}'
```

---

### **Add Card Mock Transactions Request**

```bash
curl --location 'http://localhost:8081/kapitek-card/transactions/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <TOKEN>' \
--data '{
  "cardNumber": "2165178172740537",
  "totalNumberOfOutGoingTransactions": 2,
  "totalNumberOfIncomingTransactions": 2,
  "rangeBetweenStartDate": "2024-01-01T00:00:00",
  "rangeBetweenEndDate": "2026-02-24T23:59:59"
}'
```

If successful, the service returns:

```json
true
```

---

## 📊 Step 3 — Retrieve Aggregated & Categorized Transactions

Once:

- The customer exists  
- Transactions have been created  

You can retrieve aggregated and categorized transactions using the `customerInfoFileKey`.

---

### **Get Aggregate Categorized Transactions Request**

```bash
curl --location 'http://localhost:8082/transactions/account/{customerInfoFileKey}/aggregated/months/12' \
--header 'Authorization: Bearer <TOKEN>'
```

### **Get Aggregate Categorized Transactions Response**

```json
{
  "transactions": [
    {
      "date": "2025-06-30",
      "description": "Cash Deposit: Shaunda Santo Domingo",
      "category": "Cash Deposit",
      "moneyIn": "2955.9800",
      "moneyOut": "0",
      "fee": 0.0000
    },
    {
      "date": "2025-06-22",
      "description": "Payment Received: Shanon Wunsch",
      "category": "Other Income",
      "moneyIn": "6714.4200",
      "moneyOut": "0",
      "fee": 0.0000
    }
  ],
  "summary": null
}
```

---

### **Get Aggregate Categorized Transactions With Summary Request**

```bash
curl --location 'http://localhost:8082/transactions/account/{customerInfoFileKey}/aggregated-summary/months/24' \
--header 'Authorization: Bearer <TOKEN>'
```

### **Get Aggregate Categorized Transactions With Summary Response**

```json
{
  "transactions": [
    {
      "date": "2025-06-30",
      "description": "Cash Deposit: Shaunda Santo Domingo",
      "category": "Cash Deposit",
      "moneyIn": "2955.9800",
      "moneyOut": "0",
      "fee": 0.0000
    }
  ],
  "summary": {
    "moneyInSummary": {
      "Interest": 6863.1400,
      "Cash Deposit": 2955.9800,
      "Other Income": 6714.4200
    },
    "moneyOutSummary": {
      "Cash Withdrawal": 6339.4600,
      "Digital Payment": 9022.4700
    }
  }
}
```

The **summary section** groups transactions into:

- **Money In**
- **Money Out**

Each category contains calculated totals for the specified time range.

---

## BASE URL NEEDED TO COMMUNICATE & LOGIN TO SPECIFIC USED SERVICES
### Keycloak
1. Base url: localhost:8080
2. Postman placeholder -> {{KEY_CLOAK_BASE_URL}} -> localhost:8080

### Kapitek Core Banking (Sort of a gateway for Exposing Account Service, Card Service & and Customer Info File Service)
1. Base url: **localhost:8081**
2. Postman placeholder -> {{KAPITEK_ACCOUNT_SERVICE_BASE_URL}} -> localhost:8081

### Kapitek Tansaction Aggregate & Categorize service
1. Base url: **localhost:8082**
2. Postman placeholder -> {{KAPITEK_TX_AGGREGATE_SERVICE_BASE_URL}} -> localhost:8082

### Kapitek Secrets Vault
1. Base url: **localhost:8200** (for login in to UI purpose and manage secrets. On docker the applications connect using the vault service name **vault**)
2. Token: **root** (This is also for vault UI secrets management. The The method on login should be **Token**, then provide the token provide.)

### Open Api (Swagger Documentation)
1. Url: http://localhost:8082/swagger-ui/index.html

### Aggregate Service H2 Database Access
1. Base url: **localhost:8082/h2/console**
2. Free access no Beaurer auth required. You just need to know the username and password of the h2 in dababase)
3. **H2 Username**: kapitek
4. **H2 Password**: kapitek

## 📖 API Documentation

For complete endpoint documentation, detailed request/response schemas, and additional examples, refer to the Swagger UI available once all services are running.
