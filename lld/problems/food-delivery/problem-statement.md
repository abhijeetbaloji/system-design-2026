# Problem Statement: Online Food Delivery System (Zomato / Swiggy)

## 1. Original Problem Statement

Design a food delivery application like Zomato where customers can browse restaurants and menus, add items to a cart, place and pay for orders, track order status, and restaurants and delivery partners can manage orders and deliveries.

---

## 2. Problem Interpretation

In Low-Level Design (LLD), a food delivery platform is a multi-actor, transactional marketplace system testing:
- **Core Domain Modeling**: Representing distinct business entities (`Customer`, `Restaurant`, `MenuItem`, `Cart`, `Order`, `Payment`, `DeliveryPartner`, `Location`) with clear boundaries and high cohesion.
- **Cart & Order Integrity**: Enforcing key business invariants (e.g., cart items must belong to a single restaurant, price snapshots in orders must be immutable to shield placed orders from future menu price modifications).
- **State Machine & Lifecycle Management**: Modeling the multi-step lifecycle of an order:
  $$\text{PLACED} \rightarrow \text{CONFIRMED} \rightarrow \text{PREPARING} \rightarrow \text{READY\_FOR\_PICKUP} \rightarrow \text{OUT\_FOR\_DELIVERY} \rightarrow \text{DELIVERED}$$
  (with guarded cancellation paths before food preparation begins).
- **Dispatching & Strategy Abstractions**: Assigning delivery partners based on configurable matching strategies (e.g., nearest available partner, highest-rated partner) without coupling order orchestration to spatial search algorithms.
- **Dynamic Pricing & Discounts**: Decoupling the calculation of delivery fees (distance-based, surge) and promotional discounts from core order models.
- **Real-Time Event Notification (Observer Pattern)**: Informing all three interested parties (Customer, Restaurant, Delivery Partner) whenever an order's status transitions.
- **Concurrency & Thread Safety**: Preventing race conditions when multiple delivery partners attempt to accept the same order simultaneously, or when multiple requests mutate an order's state.

---

## 3. Ambiguities Resolved for Implementation

1. **Single-Restaurant vs. Multi-Restaurant Cart**:
   - *Resolution*: Consistent with real-world platforms (Zomato, Swiggy, Uber Eats), a cart is strictly bound to a single restaurant at any time. Adding an item from a different restaurant throws a domain exception or requires explicitly clearing the cart.
2. **Order Price Snapshotting**:
   - *Resolution*: When an order is placed, an immutable snapshot of item names, unit prices, and applied fees is saved into `OrderItem` and `OrderBill`. Subsequent updates to restaurant menu prices never affect existing orders.
3. **Delivery Partner Assignment Flow**:
   - *Resolution*: The system supports algorithmic dispatching where eligible delivery partners are matched using an extensible strategy (`DeliveryPartnerMatchingStrategy`), and the assigned partner can accept or complete deliveries.
4. **Order Cancellation Policy**:
   - *Resolution*: Orders can only be cancelled while in the `PLACED` or `CONFIRMED` states. Once food preparation begins (`PREPARING`), cancellation is disallowed to prevent kitchen food wastage.

---

## 4. Explicit Assumptions

1. **In-Memory Domain Scope**: The core LLD executes within a single JVM process. Network transport (WebSockets, HTTP gateways), distributed caching (Redis), and distributed consensus are High-Level Design (HLD) concerns and are kept out of the core object model.
2. **Spatial Modeling**: Locations are modeled with 2D coordinates `(latitude, longitude)`, and distances are calculated using standard Euclidean or Haversine distance functions for delivery fee and dispatch computations.
3. **Currency & Precision**: Monetary values are represented using `BigDecimal` (or scaled double/long) to prevent floating-point rounding errors in pricing and discounts.
4. **Synchronous Payment Gateway Abstraction**: Payments are verified synchronously via a pluggable `PaymentProcessor` interface, simulating third-party gateway authorization.

---

## 5. Out of Scope

1. GPS streaming, real-time map rendering, and turn-by-turn road navigation.
2. Customer reviews, star rating submissions, and photo uploads.
3. Promo code fraud detection and marketing campaign management engines.
4. Multi-region database replication, event buses (Kafka), and microservice network plumbing.
