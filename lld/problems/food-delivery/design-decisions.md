# Architectural & Design Decisions: Online Food Delivery System (Zomato / Swiggy)

## D1 — Single-Restaurant Cart Boundary vs. Multi-Restaurant Cart

### Decision
Enforce that a customer's `Cart` can only contain items from a single `Restaurant` at any given time. Attempting to add an item from a different restaurant throws a `RestaurantMismatchException` unless the cart is explicitly cleared.

### Why?
In real-world food delivery (Zomato, Swiggy, DoorDash), restaurants are physically distinct pickup locations. Allowing a single order with items from multiple restaurants creates complex multi-pickup dispatch routing, divergent preparation timelines, doubled delivery fees, and order fulfillment hazards. Enforcing the boundary at the `Cart` level prevents invalid orders before checkout.

### Alternative
Allow a unified cart that automatically splits into sub-orders per restaurant at checkout.

### Why not?
Multi-restaurant splitting introduces order-orchestration complexity, separate delivery fee schedules, partial delivery tracking, and independent payment allocations that distract from core low-level object-oriented design without adding conceptual value.

### Consequence
The cart must store a `restaurantId` reference that is set on the first added item and reset when the cart becomes empty.

---

## D2 — Immutable OrderItem Snapshots vs. Direct MenuItem References

### Decision
When an `Order` is created from a `Cart`, copy menu item data into immutable `OrderItem` objects containing snapshot values: `menuItemId`, `itemName`, `unitPrice`, and `quantity`.

### Why?
Restaurants frequently update menu item names, descriptions, and prices. If an `Order` holds direct references to `MenuItem` objects, any subsequent price adjustment by the restaurant owner will retroactively corrupt historical order receipts and accounting totals.

### Alternative
Store references to `MenuItem` directly inside `Order`.

### Why not?
Violates data integrity and auditability. Historical orders must remain immutable regardless of future menu lifecycle changes.

### Consequence
Slight memory allocation to create `OrderItem` records upon order placement, but guarantees complete immutability of historical orders.

---

## D3 — Strategy Pattern for Delivery Fee & Discount Calculation vs. Hardcoded Logic

### Decision
Use the **Strategy Pattern** for delivery fee calculation (`DeliveryFeeStrategy`) and promotional discount evaluation (`DiscountStrategy`).

### Why?
Pricing and fee rules vary dynamically based on business conditions:
- Standard distance-based fee (e.g., $1.50 per km)
- Surge / peak hour multiplier
- Free delivery on orders over a threshold
- Flat coupons vs. percentage discounts with maximum caps

Encapsulating these variations behind strategy interfaces allows the checkout and billing engine to remain closed for modification (OCP) while easily accommodating new marketing and pricing policies.

### Alternative
A monolithic calculation method inside `OrderService` with nested `if-else` blocks for surge, distance tiers, and promo codes.

### Why not?
Leads to conditional explosion, tight coupling, and difficult unit testing. Adding or changing a pricing rule risks breaking unrelated billing calculations.

### Consequence
The billing calculation delegates to injected strategies, requiring clean strategy interfaces and contextual parameters (`Cart`, distance, subtotal).

---

## D4 — Strategy Pattern for Delivery Partner Dispatching vs. Greedy Matching

### Decision
Decouple the assignment of delivery partners to orders using a `DeliveryPartnerMatchingStrategy` interface (with implementations such as `NearestPartnerStrategy` and `HighestRatedPartnerStrategy`).

### Why?
Dispatching algorithms represent a major axis of variation in logistics platforms. Matching might prioritize physical distance, partner rating, vehicle capacity, or round-robin fairness. Using a strategy allows the dispatch service to swap matching logic dynamically or test different dispatch behaviors in isolation.

### Alternative
Hardcoding a loop in `DeliveryService` that picks the first partner where `isAvailable == true`.

### Why not?
Tightly couples the order fulfillment flow to a rudimentary matching heuristic, making it impossible to support proximity-based matching, surge routing, or driver preference ranking without rewriting core service code.

### Consequence
The dispatch service passes available candidates and order context to the strategy, which returns an `Optional<DeliveryPartner>`.

---

## D5 — Observer Pattern for Order Lifecycle Notifications vs. Direct Call Chains

### Decision
Implement the **Observer Pattern** (`OrderSubject` / `OrderObserver`) to notify interested stakeholders (`CustomerNotifier`, `RestaurantNotifier`, `DeliveryPartnerNotifier`, `OrderAuditLogger`) upon order state transitions.

### Why?
When an order transitions from `PLACED` to `PREPARING` to `OUT_FOR_DELIVERY`, multiple disjoint systems must react:
- Customer receives push notifications / SMS.
- Restaurant dashboard updates kitchen queue.
- Delivery partner receives pickup dispatch alerts.
- Audit logger records state timestamp history for SLAs.

Hardcoding these notification calls directly inside the order state transition method violates the **Single Responsibility Principle (SRP)** and tightly couples domain state logic to notification mechanisms.

### Alternative
Directly invoking `smsService.send()`, `pushService.notify()`, and `restaurantApp.alert()` inside `order.updateStatus()`.

### Why not?
Every new notification channel or subscriber requires modifying core order management code, violating OCP and making unit tests dependent on external communication stubs.

### Consequence
Subscribers register with the order subject or event dispatcher, decoupling status progression from notification fan-out.

---

## D6 — Strategy Pattern for Payment Processing vs. Hardcoded Switch Statement

### Decision
Define a `PaymentProcessor` interface implemented by `CreditCardPaymentProcessor`, `UpiPaymentProcessor`, `WalletPaymentProcessor`, and `CashOnDeliveryPaymentProcessor`, resolved via a `PaymentProcessorFactory` or dependency injection.

### Why?
Payment methods have distinct validation rules, fees, and authorization protocols. The checkout workflow should only care that a payment contract was satisfied (`PaymentResult process(PaymentRequest request)`), not how a specific gateway communicates.

### Alternative
A `switch (paymentMethod)` block inside the checkout service calling different third-party SDK methods.

### Why not?
Violates the Open/Closed Principle. Adding a new payment provider (e.g., Apple Pay, Klarna) requires editing the core checkout engine.

### Consequence
Payment methods are pluggable, interchangeable, and independently testable with mock processors.

---

## D7 — Guarded State Machine vs. Free-Form Status Mutation

### Decision
Model `OrderStatus` transitions with an explicit transition validator (`OrderStatus.canTransitionTo(nextStatus)` or state-machine methods on `Order`).

### Why?
Food delivery involves a strict, irreversible physical workflow. Food cannot be marked `DELIVERED` before it is `OUT_FOR_DELIVERY`, nor can an order be `CANCELLED` after cooking has begun (`PREPARING`). Allowing arbitrary setters like `order.setStatus(status)` allows invalid states to corrupt the domain.

### Alternative
Public setter `order.setStatus(OrderStatus status)` without validation.

### Why not?
Allows illegal state transitions (e.g., cancelling an order after delivery, or jumping straight from placed to delivered), leading to severe business and fulfillment bugs.

### Consequence
Attempting an invalid state transition throws an `IllegalStateException` or `InvalidOrderStateException`, keeping domain invariants intact.

---

## D8 — In-Memory Thread-Safe Repositories vs. Relational/NoSQL Database for LLD

### Decision
Use in-memory repositories backed by `ConcurrentHashMap` with atomic operations for the core LLD levels (`good`, `interview-ready`, and `spring-boot`).

### Why?
In an LLD interview or learning repository, the focus is on object-oriented modeling, encapsulation, clean abstractions, and concurrency safety. Introducing PostgreSQL, JPA/Hibernate, or Docker containers introduces boilerplate and external dependencies that obscure the pure object design.

### Alternative
Configuring Spring Data JPA with an H2 or PostgreSQL database.

### Why not?
Adds ORM mapping annotations (`@Entity`, `@Table`, `@ManyToOne`) that clutter domain POJOs, introduce lazy loading / session pitfalls, and shift focus away from object-oriented design and design patterns.

### Consequence
Clean repository interfaces (`OrderRepository`, `RestaurantRepository`, `DeliveryPartnerRepository`) that can be replaced with SQL/NoSQL implementations in real production without changing domain logic.
