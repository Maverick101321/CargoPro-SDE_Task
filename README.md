# 🚚 Transport Management System (TMS) Backend Assignment

## 🎯 Objective

[cite_start]This project implements the backend system for a Transport Management System using **Spring Boot 3.2+** and **PostgreSQL** [cite: 4, 113-115]. [cite_start]The focus is on handling real-world logistics challenges, including complex state transitions, capacity management, and concurrency control[cite: 5, 53].

## 🛠️ Technical Stack & Dependencies

* [cite_start]**Language:** Java 17+ [cite: 114]
* [cite_start]**Framework:** Spring Boot 3.2+ [cite: 114]
* [cite_start]**Database:** PostgreSQL [cite: 115]
* [cite_start]**ORM:** Spring Data JPA / Hibernate [cite: 115]
* [cite_start]**Architecture:** Layered (Controller -> DTO -> Service -> Repository -> Entity) [cite: 118]
* [cite_start]**Concurrency Control:** Optimistic Locking (`@Version`) [cite: 75, 123]

---

## 🚀 Setup & Installation

### Prerequisites

1.  Java Development Kit (JDK) 17+
2.  PostgreSQL Server running locally (Default port 5432)
3.  Maven (or use the wrapper included)

### Database Configuration

1.  Create a PostgreSQL database named `db_tms`.
2.  Update the connection details in `src/main/resources/application.properties` with your credentials:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/db_tms
    spring.datasource.username=[YOUR POSTGRES USERNAME]
    spring.datasource.password=[YOUR POSTGRES PASSWORD]
    ```

### Running the Application

1.  Clone the repository.
2.  Run the application using your IDE or Maven:
    ```bash
    ./mvnw spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

---

## 📝 Required Documentation

[cite_start]This section fulfills the mandatory documentation requirements for the assignment[cite: 141].

### 1. Database Schema Diagram

*Paste your schema diagram here or provide a link to the image.*

[cite_start]*(Note: This diagram is required to visualize the relationships between Load, Bid, Booking, and Transporter entities)* [cite: 142]

### 2. API Documentation (Swagger/Postman)

The full API documentation (including request/response schemas and examples for the 15 endpoints) can be found at:

* **Swagger/OpenAPI URL:** `http://localhost:8080/swagger-ui.html` (Available after adding Swagger dependency)
* [cite_start]**Postman Collection Link:** [Link to your exported Postman collection] [cite: 143]

### 3. Test Coverage Screenshot

[cite_start]*Paste a screenshot of your test coverage report here.* [cite: 144]

---

## 💡 Implementation Details & Business Rules

### Core Entities

[cite_start]The system revolves around four core entities: **Load**, **Transporter**, **Bid**, and **Booking**[cite: 6].

### Critical Business Rule Implementation

| Rule | Implementation Strategy |
| :--- | :--- |
| [cite_start]**Capacity Validation** [cite: 54] | Implemented in `BidService.createBid()`. Checks `trucksOffered <= availableTrucks` for the specified `truckType`. [cite_start]Truck capacity is updated transactionally upon `BOOKED` or `CANCELLED` status changes in the `BookingService`[cite: 56, 59, 61]. |
| [cite_start]**Status Transitions** [cite: 62] | [cite_start]Handled within the Service layer using validation checks and throwing `InvalidStatusTransitionException` for illegal moves (e.g., Cannot bid on CANCELLED or BOOKED loads)[cite: 68, 125]. |
| [cite_start]**Multi-Truck Allocation** [cite: 70] | The `Load` entity tracks the total `numOfTrucks` required. The `BookingService` ensures that `SUM(allocatedTrucks)` across all CONFIRMED bookings does not exceed `numOfTrucks`. [cite_start]The load status moves to `BOOKED` only when `remainingTrucks == 0` [cite: 71-74]. |
| [cite_start]**Concurrent Booking** [cite: 75] | [cite_start]Prevention is enforced using the **Optimistic Locking** mechanism (`@Version` field on the `Load` entity)[cite: 76, 123]. [cite_start]Conflicts result in a `LoadAlreadyBookedException` (HTTP 409 Conflict)[cite: 77, 127]. |
| [cite_start]**Best Bid Calculation** [cite: 78] | [cite_start]Implemented in `LoadService.getBestBids()`, sorting bids by the calculated **Score**: $$ \text{Score} = \left(\frac{1}{\text{proposedRate}}\right) \times 0.7 + \left(\frac{\text{rating}}{5}\right) \times 0.3 $$ [cite: 80, 81] |

---

## 💻 API Endpoints Summary

A summary of the 15 required endpoints:

| Domain | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Load** | POST | `/load` | [cite_start]Create new load (status: POSTED) [cite: 84, 85] |
| **Load** | GET | `/load` | [cite_start]Search/Filter loads by shipperId/status (with pagination) [cite: 86] |
| **Load** | GET | `/load/{loadId}` | [cite_start]Get load details with active bids [cite: 87] |
| **Load** | PATCH | `/load/{loadId}/cancel` | [cite_start]Cancel load (status validation applied) [cite: 88, 90] |
| **Load** | GET | `/load/{loadId}/best-bids` | [cite_start]Get sorted bid suggestions (using Score formula) [cite: 89] |
| **Transporter**| POST | `/transporter` | [cite_start]Register transporter/set initial capacity [cite: 92, 93] |
| **Transporter**| GET | `/transporter/{transporterId}` | [cite_start]Get transporter details [cite: 94] |
| **Transporter**| PUT | `/transporter/{transporterId}/trucks` | [cite_start]Update available truck capacity [cite: 95] |
| **Bid** | POST | `/bid` | [cite_start]Submit new bid (capacity/status validation applied) [cite: 97, 99] |
| **Bid** | GET | `/bid` | [cite_start]Filter bids by loadId/transporterId/status [cite: 100, 105] |
| **Bid** | GET | `/bid/{bidId}` | [cite_start]Get bid details [cite: 101] |
| **Bid** | PATCH | `/bid/{bidId}/reject` | [cite_start]Reject specific bid [cite: 101, 104] |
| **Booking** | POST | `/booking` | [cite_start]Accept bid, create booking (handles concurrency, deducts trucks) [cite: 103, 106, 107] |
| **Booking** | GET | `/booking/{bookingId}` | [cite_start]Get booking details [cite: 108] |
| **Booking** | PATCH | `/booking/{bookingId}/cancel` | [cite_start]Cancel booking (restores trucks, updates load status) [cite: 109, 110, 111] |