# Interview-Ready Design: Online Food Delivery System

## 1. Overview & Whiteboard Strategy

The Interview-Ready Design is the primary interview solution for an online food delivery platform (Zomato, Swiggy, Uber Eats). It builds upon the clean domain model of Good Design and incorporates targeted, justified design patterns to handle the core variations encountered in real-world technical interviews:

1. **Dynamic Delivery Fee Calculation** via Strategy Pattern (`DeliveryFeeStrategy`).
2. **Promotional Discounts & Coupons** via Strategy Pattern (`DiscountStrategy`).
3. **Delivery Partner Dispatching Heuristics** via Strategy Pattern (`DeliveryPartnerMatchingStrategy`).
4. **Order State Machine Notifications** via Observer Pattern (`OrderObserver` and `OrderEventPublisher`).
5. **Payment Gateway Abstraction & Resolution** via Strategy + Factory Pattern (`PaymentProcessorFactory`).
6. **Thread-Safe Domain Repositories** backed by `ConcurrentHashMap`.

```
               ┌──────────────────────┐
               │ OrderCheckoutService │
               └──────────┬───────────┘
     ┌────────────────────┼────────────────────┬────────────────────┐
     ▼                    ▼                    ▼                    ▼
┌──────────────┐   ┌──────────────┐   ┌─────────────────┐   ┌────────────────┐
│DeliveryFee-  │   │Discount-     │   │PaymentProcessor-│   │OrderEvent-     │
│Strategy      │   │Strategy      │   │Factory          │   │Publisher       │
└──────┬───────┘   └──────┬───────┘   └────────┬────────┘   └───────┬────────┘
       ▼                  ▼                    ▼                    ▼
[Distance/Surge]   [Flat/Percent]     [UPI/Card/COD]        [Customer/Kitchen/
                                                             Driver/AuditLog]
```

---

## 2. Design Patterns Earned by the Problem

### Pattern 1: Strategy Pattern (Delivery Fee Calculation)
- **1. What problem exists?** Delivery fees vary across orders based on physical distance, bad weather, rush hours (surge pricing), and customer loyalty tier (free delivery thresholds).
- **2. What varies?** The mathematical calculation algorithm for the delivery fee.
- **3. Why is the pattern appropriate?** It isolates pricing algorithms from the checkout workflow, allowing dynamic composition (e.g. `SurgeDeliveryFeeStrategy` wrapping `DistanceBasedDeliveryFeeStrategy`).
- **4. What simpler solution was considered?** Hardcoding a fee formula directly in `OrderCheckoutService`.
- **5. Why is the simpler solution insufficient?** Modifying surge rates or adding rain-surge rules would require repeatedly editing `OrderCheckoutService`, violating OCP.
- **6. What complexity does the pattern add?** Introduces the `DeliveryFeeStrategy` interface and concrete strategy classes.
- **7. When would I NOT use this pattern?** If delivery fees are strictly flat and unchanging across all restaurants and distances.

### Pattern 2: Strategy Pattern (Promotional Discounts)
- **1. What problem exists?** Marketing campaigns launch diverse promotions: percentage discounts with maximum caps (`PERCENT20`), flat dollar coupons (`FLAT50`), or no discount.
- **2. What varies?** The discount calculation rule applied against the order subtotal.
- **3. Why is the pattern appropriate?** Allows passing interchangeable discount strategies to `OrderCheckoutService.checkout()` without branching code.
- **4. What simpler solution was considered?** A nested `switch (couponCode)` block inside the checkout service.
- **5. Why is the simpler solution insufficient?** Creates conditional explosion and tightly couples the checkout service to marketing campaign rules.
- **6. What complexity does the pattern add?** Strategy interface and separate discount classes.
- **7. When would I NOT use this pattern?** If the platform offers no discounts or coupons.

### Pattern 3: Strategy Pattern (Delivery Partner Dispatching)
- **1. What problem exists?** Selecting which available courier to assign to an order depends on business objectives: minimizing pickup delay (nearest partner) vs. maximizing customer satisfaction (highest-rated partner).
- **2. What varies?** The courier selection algorithm applied against the list of available delivery partners.
- **3. Why is the pattern appropriate?** Allows swapping `NearestPartnerMatchingStrategy` with `HighestRatedPartnerMatchingStrategy` at runtime or in response to peak load.
- **4. What simpler solution was considered?** Looping through partners and selecting the first available driver.
- **5. Why is the simpler solution insufficient?** Greedy first-available dispatching results in high pickup transit times, cold food, and dissatisfied customers.
- **6. What complexity does the pattern add?** `DeliveryPartnerMatchingStrategy` interface and comparator logic.
- **7. When would I NOT use this pattern?** If couriers are dedicated to specific restaurants or deliveries are purely customer-pickup.

### Pattern 4: Observer Pattern (Real-Time Order Lifecycle Notifications)
- **1. What problem exists?** As an order progresses through its physical lifecycle (`PLACED` $\rightarrow$ `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `READY_FOR_PICKUP` $\rightarrow$ `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED`), multiple external parties must be notified immediately.
- **2. What varies?** The downstream subscribers (customer push notifications, kitchen display alerts, driver dispatch alerts, compliance audit logs) and communication channels.
- **3. Why is the pattern appropriate?** The order state machine only transitions internal status; subscribers register independently with `OrderEventPublisher`.
- **4. What simpler solution was considered?** Calling SMS service, kitchen service, and driver service sequentially in each status method.
- **5. Why is the simpler solution insufficient?** Violates SRP; any notification failure could block order state transitions, and adding a new channel (e.g. email receipt) would require modifying state services.
- **6. What complexity does the pattern add?** An `OrderObserver` interface and event publisher with thread-safe observer collection.
- **7. When would I NOT use this pattern?** In batch/offline processing where real-time stakeholder notification is not needed.

---

## 3. SOLID Principles in Practice

- **Single Responsibility Principle (SRP)**:
  - `OrderCheckoutService` is responsible solely for checkout coordination and billing snapshotting.
  - `OrderFulfillmentService` manages restaurant kitchen state transitions.
  - `DeliveryDispatchService` handles courier assignment and transit updates.
- **Open/Closed Principle (OCP)**:
  - `DeliveryFeeStrategy`, `DiscountStrategy`, and `DeliveryPartnerMatchingStrategy` allow introducing new business rules by creating new classes without altering existing services.
- **Liskov Substitution Principle (LSP)**:
  - `SurgeDeliveryFeeStrategy` can transparently substitute for `DistanceBasedDeliveryFeeStrategy` in `OrderCheckoutService` without altering correctness.
  - `HighestRatedPartnerMatchingStrategy` cleanly substitutes for `NearestPartnerMatchingStrategy`.
- **Interface Segregation Principle (ISP)**:
  - `OrderObserver` exposes a single focused method: `onOrderStatusChanged()`.
  - Strategy interfaces contain exactly one method tailored to their calculation.
- **Dependency Inversion Principle (DIP)**:
  - `OrderCheckoutService` depends on abstract strategy interfaces (`DeliveryFeeStrategy`, `DiscountStrategy`) and `OrderEventPublisher`, injected via constructor.

---

## 4. Concurrency & Thread Safety

1. **Cart Modifications**:
   - `Cart.addItem()`, `Cart.removeItem()`, and `Cart.clear()` are `synchronized` methods ensuring that concurrent modifications from multiple client tabs maintain single-restaurant and quantity consistency.
2. **Order State Transitions**:
   - `Order.transitionTo()` is `synchronized` to ensure atomic state updates and prevent duplicate or conflicting status progressions.
3. **Courier Assignment**:
   - `DeliveryDispatchService.dispatchPartner()` is `synchronized` to ensure an available courier is assigned to only one order atomically.
4. **Repositories**:
   - `RestaurantRepository`, `OrderRepository`, `DeliveryPartnerRepository`, and `CustomerRepository` use `ConcurrentHashMap` for lock-free read operations and safe concurrent writes.

---

## 5. What Was Kept Intentionally Simple

1. **Euclidean Distance**:
   Instead of integrating real-world map providers (Google Directions API / OSRM), coordinate distance uses planar Euclidean geometry ($\sqrt{\Delta x^2 + \Delta y^2}$). This provides deterministic, instant unit tests without external network dependencies.
2. **Synchronous Notification Dispatch**:
   `OrderEventPublisher` iterates over observers in-process with exception isolation rather than sending messages to Kafka/RabbitMQ.
3. **In-Memory Repositories**:
   Repositories store Java domain entities in thread-safe memory maps rather than relying on JPA/Hibernate or SQL schemas.
