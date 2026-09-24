# Low-Level Design: Online Food Delivery System (Zomato / Swiggy)

A comprehensive study package for the Low-Level Design (LLD) of an online food delivery platform like Zomato, Swiggy, or Uber Eats.

This study package demonstrates the evolution of object-oriented design across four distinct stages:
1. **Bad Design**: A realistic monolithic implementation with clear design flaws.
2. **Good Design**: A clean, cohesive object-oriented design solving the core requirements.
3. **Interview-Ready Design**: An appropriately extensible design incorporating targeted design patterns for whiteboard interviews.
4. **Spring Boot / Production-Oriented Design**: A layered web application mapping the domain design to modern enterprise standards.

---

## Directory Structure

```
lld/problems/food-delivery/
├── problem-statement.md               # Scope, ambiguities, assumptions, out-of-scope
├── requirements/
│   └── requirements.md                # Functional & non-functional requirements, use cases
├── design-decisions.md                # Explicit architectural and design decisions (D1–D8)
├── diagrams/
│   ├── bad.mmd                        # Mermaid diagram for Bad Design
│   ├── good.mmd                       # Mermaid diagram for Good Design
│   ├── interview-ready.mmd            # Mermaid diagram for Interview-Ready Design
│   └── spring-boot.mmd                # Mermaid diagram for Spring Boot Architecture
├── bad/                               # Monolithic flawed implementation
│   ├── pom.xml
│   ├── README.md
│   └── src/
├── good/                              # Clean object-oriented implementation
│   ├── pom.xml
│   ├── README.md
│   └── src/
├── interview-ready/                   # Primary interview solution with patterns
│   ├── pom.xml
│   ├── README.md
│   └── src/
├── spring-boot/                       # Production-oriented Spring Boot 3 application
│   ├── pom.xml
│   ├── README.md
│   └── src/
└── README.md                          # Main documentation & 4-level comparison
```

---

## Design Evolution Across the Four Levels

### 1. Bad Design
- **What works**:
  - Successfully registers restaurants, menus, carts, and delivery partners.
  - Can place an order with hardcoded payments, assign a driver, and transition statuses.
- **What is problematic**:
  - **God Class (`FoodDeliveryMonolithManager`)**: Collapses catalog, carts, checkout, fees, drivers, and notifications into one 500-line monolith.
  - **Retroactive Price Mutation**: `Order` stores live references to `MenuItem`. Mutating a menu price tomorrow retroactively corrupts past historical order totals.
  - **Cross-Restaurant Cart Contamination**: `Cart` fails to enforce the single-restaurant boundary; items from multiple restaurants can be placed in one cart.
  - **Unchecked State Transitions**: `Order.setStatus()` allows arbitrary status jumps (e.g. `DELIVERED` $\rightarrow$ `CANCELLED`).
  - **Conditional Explosion**: Hardcoded `if-else` chains for payments, fees, and greedy partner assignment.

### 2. Good Design
- **What was improved**:
  - **Separation of Concerns**: Decomposed the God class into focused services (`RestaurantService`, `CartService`, `OrderService`, `DeliveryService`).
  - **Single-Restaurant Cart Invariant**: `Cart.addItem()` strictly validates that all items belong to the same restaurant, throwing `RestaurantMismatchException` otherwise.
  - **Immutable Snapshots**: `OrderItem` and `OrderBill` snapshot item details and prices at checkout, completely immunizing historical orders from catalog edits.
  - **Guarded State Machine**: `OrderStatus.canTransitionTo()` enforces valid physical progression and disallows cancellation once cooking starts (`PREPARING`).
  - **Payment Abstraction**: `PaymentProcessor` interface decouples `OrderService` from concrete payment gateway mechanics.
  - **Value Objects**: Introduced `Location` record with domain `distanceTo()` computation, eliminating primitive obsession.

### 3. Interview-Ready Design
- **What flexibility was added and why**:
  - *Interview-Ready is an appropriate design for these requirements and interview constraints.*
  - **Delivery Fee Strategy (`DeliveryFeeStrategy`)**: Encapsulates delivery fee algorithms (`DistanceBasedDeliveryFeeStrategy`, `SurgeDeliveryFeeStrategy` with peak-hour multiplier).
  - **Discount Strategy (`DiscountStrategy`)**: Allows pluggable promotional campaigns (`PercentageDiscountStrategy`, `FlatDiscountStrategy`, `NoDiscountStrategy`).
  - **Delivery Partner Dispatch Strategy (`DeliveryPartnerMatchingStrategy`)**: Swappable courier heuristics (`NearestPartnerMatchingStrategy` vs. `HighestRatedPartnerMatchingStrategy`).
  - **Lifecycle Notification Observers (`OrderObserver`, `OrderEventPublisher`)**: Decouples the order state machine from downstream alert channels (`CustomerNotifier`, `RestaurantNotifier`, `DeliveryPartnerNotifier`, `OrderAuditLogger`).
  - **Payment Factory**: Resolves `PaymentProcessor` via strongly-typed `PaymentMethod` enum.
  - **Thread-Safe Repositories**: Encapsulates storage in `ConcurrentHashMap` with atomic updates.

### 4. Spring Boot / Production-Oriented Design
- **How the design maps to an application**:
  - **Pure Domain Preserved**: Domain models (`Order`, `Cart`, `MenuItem`, `OrderStatus`, strategies) remain pure Java POJOs without framework pollution.
  - **Controllers**: Expose RESTful endpoints for restaurant browsing, cart management, checkout, order fulfillment, and courier dispatch.
  - **Application Services**: Coordinate use-case transactions and translate between HTTP DTOs and domain models.
  - **Perimeter Validation**: Jakarta Validation annotations (`@NotBlank`, `@Min`, `@DecimalMax`) protect API inputs.
  - **Centralized Exception Handling**: `@RestControllerAdvice` maps domain exceptions to HTTP status codes (`400`, `402`, `404`).

---

## Comparison Table

| Dimension | Bad Design | Good Design | Interview-Ready Design | Spring Boot Design |
| :--- | :--- | :--- | :--- | :--- |
| **Architecture** | Monolithic God Class | Domain Services + Clean POJOs | Strategy + Observer + Repositories | Layered Enterprise (Controller/Service/Repo) |
| **Cart Invariants** | Unenforced (Multi-restaurant allowed) | Enforced via `Cart.addItem()` | Enforced via `Cart.addItem()` | Enforced + Handled via REST error response |
| **Price Snapshots** | Leaky live references (Mutable) | Immutable `OrderItem` record | Immutable `OrderItem` record | Immutable record mapped to DTOs |
| **Fee Calculation** | Hardcoded Euclidean formula | Hardcoded linear formula | `DeliveryFeeStrategy` (Distance & Surge) | Injected Strategy Bean |
| **Discounts** | None | None | `DiscountStrategy` (Flat & Percentage) | Dynamic Strategy Resolution from Promo Code |
| **Courier Dispatch** | Greedy loop (first-available) | Nearest available partner | `DeliveryPartnerMatchingStrategy` | Dispatch endpoint using injected strategy |
| **Notifications** | Direct `System.out.println` | None / inlined | `OrderObserver` fan-out with audit log | Observer beans broadcasting lifecycle events |
| **State Machine** | Unconstrained string setter | Guarded enum transitions | Guarded enum transitions + Observers | Guarded transitions + REST status endpoints |
| **Testability** | Hard to isolate; tightly coupled | 100% pure Java unit tests | Unit tests for strategies & observers | MockMvc integration tests & unit tests |

---

## How to Build and Run Tests

Each module is an independent Maven project targeting Java 21:

```bash
# 1. Test Bad Design
cd lld/problems/food-delivery/bad
mvn clean test

# 2. Test Good Design
cd ../good
mvn clean test

# 3. Test Interview-Ready Design
cd ../interview-ready
mvn clean test

# 4. Test Spring Boot Design
cd ../spring-boot
mvn clean test
```
