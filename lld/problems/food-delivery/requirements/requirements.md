# Requirements Analysis: Online Food Delivery System (Zomato / Swiggy)

## Functional Requirements

1. **Restaurant & Menu Management**:
   - Register restaurants with location, name, cuisine, and operational status (active/inactive).
   - Maintain restaurant menus composed of `MenuItem` entries (name, description, price, food category: VEG/NON_VEG, and availability status).
   - Customers can browse active restaurants and view their menus.

2. **Cart Management**:
   - Each customer has an active shopping cart.
   - Customers can add items, change quantities, remove items, or clear the cart.
   - **Single-Restaurant Constraint**: A cart can only contain items from one restaurant at a time. Attempting to add an item from another restaurant must be rejected unless the customer clears the cart.

3. **Order Placement & Bill Calculation**:
   - Convert cart items into an active `Order`.
   - **Immutable Price Snapshot**: Capture snapshot unit prices and quantities in `OrderItem` so future menu price changes do not mutate historical orders.
   - Compute comprehensive order billing: item subtotal, delivery fee (distance-based), taxes, and applicable discounts.

4. **Payment Processing**:
   - Support multiple payment methods (e.g., Credit Card, UPI, Net Banking, Cash on Delivery).
   - Enforce payment validation during checkout. If payment fails, the order is marked failed and not passed to the restaurant.

5. **Order Lifecycle & State Transitions**:
   - Model and enforce strict order status transitions:
     - `PLACED` $\rightarrow$ Order created and payment authorized.
     - `CONFIRMED` $\rightarrow$ Restaurant acknowledges and accepts the order.
     - `PREPARING` $\rightarrow$ Kitchen starts cooking.
     - `READY_FOR_PICKUP` $\rightarrow$ Food is packed and waiting for delivery partner.
     - `OUT_FOR_DELIVERY` $\rightarrow$ Delivery partner has collected the order and is in transit.
     - `DELIVERED` $\rightarrow$ Order handed over to the customer.
     - `CANCELLED` $\rightarrow$ Allowed only while order is in `PLACED` or `CONFIRMED` state.
   - Reject invalid transitions (e.g., cannot transition from `DELIVERED` to `PREPARING`, cannot transition from `READY_FOR_PICKUP` to `CANCELLED`).

6. **Restaurant Order Fulfillment**:
   - Restaurant receives incoming placed orders.
   - Kitchen staff can accept the order (`CONFIRMED`), mark it cooking (`PREPARING`), and notify when ready (`READY_FOR_PICKUP`).

7. **Delivery Partner Dispatching & Delivery Management**:
   - Delivery partners register their availability status (`AVAILABLE`, `BUSY`, `OFFLINE`) and current geographic location.
   - System dispatches/assigns available delivery partners to orders that are being prepared or ready for pickup.
   - Assigned delivery partner marks order picked up (`OUT_FOR_DELIVERY`) and delivered (`DELIVERED`).

8. **Order Tracking & Notifications**:
   - Customers can query their order status in real time.
   - Key stakeholders (Customer, Restaurant, Delivery Partner) receive status update notifications whenever order status changes.

---

## Non-Functional Design Requirements

1. **Extensibility (Open/Closed Principle)**:
   - **Payment Methods**: Adding new payment gateways (e.g., Apple Pay, Crypto, Sodexo) must not modify the order checkout service.
   - **Delivery Fee & Discounts**: Pricing algorithms (surge pricing, distance-based tiered fee, flat discounts, coupon codes) must be interchangeable via strategy patterns.
   - **Partner Dispatching**: Algorithms for selecting delivery partners (nearest partner, highest-rated partner, batched deliveries) must be swappable without altering the order lifecycle.
2. **Encapsulation & High Cohesion**:
   - Domain objects must protect their own invariants (e.g., Cart validates restaurant consistency; Order guards valid status transitions).
3. **Thread Safety & Concurrency**:
   - Prevent race conditions during concurrent delivery partner assignment (e.g., two drivers cannot claim the same order simultaneously).
   - Ensure thread-safe status updates and cart modifications.
4. **Testability**:
   - 100% of domain logic, state transitions, pricing calculations, and dispatch algorithms must be unit-testable in pure Java with zero external dependencies.

---

## Actors

1. **Customer**: Browses menus, manages cart, places orders, makes payments, tracks order status, and receives notifications.
2. **Restaurant Manager / Kitchen Staff**: Updates menu availability, accepts/rejects orders, and updates preparation status.
3. **Delivery Partner (Driver)**: Sets availability, receives delivery assignments, picks up food from restaurants, and delivers to customers.
4. **System Dispatcher**: Coordinates automated delivery partner matching and broadcasts order lifecycle events.

---

## Core Use Cases

- **UC-1: Browse Restaurants & Menus**: Customer views available restaurants and inspects their menu items.
- **UC-2: Manage Cart**: Customer adds/updates menu items in cart, validating single-restaurant boundary.
- **UC-3: Checkout & Pay**: Customer initiates checkout; system calculates bill (subtotal, delivery fee, taxes, discount), processes payment, creates `Order` with immutable snapshots, and clears cart.
- **UC-4: Restaurant Acceptance & Prep**: Restaurant accepts order (`CONFIRMED`), begins cooking (`PREPARING`), and marks packing complete (`READY_FOR_PICKUP`).
- **UC-5: Delivery Partner Matching & Dispatch**: System evaluates available delivery partners using a matching strategy and assigns an optimal partner.
- **UC-6: Delivery Execution**: Partner accepts assignment, picks up food (`OUT_FOR_DELIVERY`), and completes delivery (`DELIVERED`).
- **UC-7: Real-Time Order Tracking & Notification**: Observers receive instant notification when status changes.
- **UC-8: Order Cancellation**: Customer or restaurant cancels order if food prep has not commenced.

---

## Assumptions

1. Delivery coordinates are represented as 2D `(latitude, longitude)` on an Euclidean/planar plane where distance is $\sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$ (or Haversine formula) for deterministic testing.
2. Each customer maintains one active cart per session.
3. Once an order is placed, cart items are cleared for the next order.
4. The system operates as a single JVM service; network transport is simulated cleanly via interfaces.

---

## Out of Scope

1. GPS live map rendering and real-time path routing.
2. Marketing analytics, referral credits, and customer loyalty tier programs.
3. Kitchen inventory and ingredient supply chain replenishment.
4. Distributed data stores, database sharding, and message brokers (Kafka/RabbitMQ).
