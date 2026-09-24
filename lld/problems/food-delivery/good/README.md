# Good Design: Online Food Delivery System

## 1. What Changed from Bad Design

The Good Design decomposes the monolithic God class into focused, cohesive domain models and services:

1. **Decomposition of the God Class**:
   - `RestaurantService`: Manages restaurant registration and menu catalogs.
   - `CartService`: Coordinates customer shopping carts.
   - `OrderService`: Orchestrates checkout, bill generation, and order lifecycle transitions.
   - `DeliveryService`: Manages delivery partners and assignment.
2. **Encapsulation of Single-Restaurant Boundary**:
   - `Cart.addItem()` strictly validates that all items added belong to the same restaurant. Attempting to add an item from another restaurant immediately throws `RestaurantMismatchException`.
3. **Immutable Price Snapshots**:
   - `OrderItem` and `OrderBill` are immutable snapshots (`record`). Future changes to `MenuItem.price` by a restaurant owner will never mutate historical order records.
4. **Guarded State Machine**:
   - `OrderStatus.canTransitionTo()` enforces legitimate lifecycle progression (`PLACED` $\rightarrow$ `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `READY_FOR_PICKUP` $\rightarrow$ `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED`), preventing illegal status jumps or regressions.
5. **Polymorphic Payment Processing**:
   - Replaced hardcoded `if-else` blocks with the `PaymentProcessor` abstraction (`CreditCardPaymentProcessor`, `UpiPaymentProcessor`, `CashOnDeliveryPaymentProcessor`).
6. **Encapsulated Value Objects**:
   - Introduced `Location` record with domain `distanceTo()` computation, eliminating raw coordinate pairs and primitive obsession.

---

## 2. Responsibilities of Each Class

| Class / Record | Responsibility |
| :--- | :--- |
| `Location` | Value object encapsulating latitude, longitude, and distance calculation. |
| `MenuItem` | Catalog item representing dish details, price, availability, and category. |
| `Menu` | Encapsulates a restaurant's item catalog with guarded accessors. |
| `Restaurant` | Represents a dining establishment; owns a `Menu` and a `Location`. |
| `Customer` | End-user profile containing identity and delivery `Location`. |
| `CartItem` | Pairs a `MenuItem` with a quantity in the customer's active shopping cart. |
| `Cart` | Enforces the single-restaurant boundary and computes item subtotal. |
| `OrderItem` | **Immutable snapshot** of item name, unit price, and quantity at order time. |
| `OrderBill` | Immutable breakdown of subtotal, delivery fee, taxes, and total amount. |
| `OrderStatus` | Enum state machine defining valid lifecycle transitions. |
| `Order` | Root entity managing order items, bill, lifecycle status, and partner ID. |
| `DeliveryPartner` | Courier entity tracking availability status, location, and assigned order. |
| `PaymentProcessor` | Interface decoupling payment authorization from order placement. |
| `RestaurantService` | Manages restaurant registration and discovery. |
| `CartService` | Coordinates cart retrieval, item additions, and cart clearing. |
| `OrderService` | Orchestrates order placement, payments, and status transitions. |
| `DeliveryService` | Manages delivery partner fleet and simple proximity dispatching. |

---

## 3. Important Relationships

- **Composition (`Restaurant *-- Menu`)**: A `Restaurant` owns its `Menu`. A menu cannot exist independently of its restaurant.
- **Aggregation (`Menu o-- MenuItem`)**: A `Menu` groups multiple `MenuItem` elements.
- **Composition (`Cart *-- CartItem`)**: A `Cart` owns its `CartItem` entries. Clearing the cart destroys the cart items.
- **Composition (`Order *-- OrderItem`)**: An `Order` owns its immutable snapshot items.
- **Realization (`PaymentProcessor <|.. CreditCardPaymentProcessor`)**: Concrete payment implementations satisfy the payment contract.
- **Dependency (`OrderService --> PaymentProcessor`)**: `OrderService` depends on the abstraction, injected via constructor.

---

## 4. Why Each Abstraction Exists

1. `PaymentProcessor`:
   - *Problem*: Payment gateways vary widely (Stripe, UPI, PayPal, Apple Pay).
   - *Justification*: Prevents `OrderService` from containing third-party SDK dependencies or nested conditional branches. New payment methods can be introduced without editing order placement code.
2. `Location`:
   - *Problem*: Latitude/longitude pairs were passed everywhere as raw primitives (`double lat, double lon`), leading to data clumps and code duplication.
   - *Justification*: Encapsulating coordinates inside a record centralizes distance math and guarantees coordinate immutability.

---

## 5. SOLID Improvements

- **SRP (Single Responsibility Principle)**:
  Each service and domain model owns exactly one responsibility. `OrderService` handles order workflows; `Cart` handles cart invariants; `PaymentProcessor` handles transaction authorization.
- **OCP (Open/Closed Principle)**:
  Adding a new payment provider (e.g., `CryptoPaymentProcessor`) requires adding a new class implementing `PaymentProcessor`, leaving `OrderService` untouched.
- **LSP (Liskov Substitution Principle)**:
  Any `PaymentProcessor` implementation can substitute for the interface without altering `OrderService` correctness.
- **ISP (Interface Segregation Principle)**:
  `PaymentProcessor` is focused and contains only the single method required for order settlement: `process(double amount, String paymentDetail)`.
- **DIP (Dependency Inversion Principle)**:
  `OrderService` depends on the `PaymentProcessor` interface rather than concrete classes like `CreditCardPaymentProcessor`.

---

## 6. Remaining Limitations

1. **Hardcoded Pricing & Fee Formulas**:
   In `OrderService`, delivery fee is calculated using a hardcoded linear formula (`BASE_DELIVERY_FEE + distance * PER_KM_FEE`). It cannot easily support surge pricing, weather-based pricing, or free delivery coupons without editing `OrderService`.
2. **Hardcoded Dispatch Logic**:
   `DeliveryService` hardcodes a single nearest-partner loop. It cannot easily switch to rating-based matching, batch dispatching, or driver preference algorithms.
3. **Coupled Notifications**:
   Stakeholder alerting (customer SMS, restaurant kitchen display, driver app notifications) is either omitted or must be called directly inside status transition methods.

---

## 7. Trade-offs

- **Memory vs. Immutability**:
  Copying `CartItem` data into new `OrderItem` snapshot instances requires a minor memory allocation per order item, but completely eliminates data corruption when restaurants update catalog prices.
- **Synchronous Simplicity**:
  Using synchronous method calls for payments and partner assignment keeps the design simple, readable, and 100% testable without introducing asynchronous complexity or message brokers.
