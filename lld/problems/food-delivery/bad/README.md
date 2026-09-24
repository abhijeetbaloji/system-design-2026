# Bad Design: Food Delivery Application

## 1. Design Overview

The Bad Design represents a typical beginner-to-intermediate monolithic implementation of an online food delivery service. All core capabilities—restaurant registration, menu maintenance, shopping cart operations, checkout, payment processing, distance/fee calculations, driver dispatching, status updating, and customer alerting—are collapsed into a single central class: `FoodDeliveryMonolithManager`.

```
                    ┌───────────────────────────────┐
                    │ FoodDeliveryMonolithManager   │ (God Class)
                    └───────────────┬───────────────┘
           ┌────────────────┼───────────────┼────────────────┐
           ▼                ▼               ▼                ▼
     ┌───────────┐    ┌───────────┐   ┌───────────┐   ┌───────────────┐
     │Restaurant │    │   Cart    │   │   Order   │   │DeliveryPartner│
     └─────┬─────┘    └─────┬─────┘   └─────┬─────┘   └───────────────┘
           ▼                ▼               ▼
     ┌───────────┐    ┌───────────┐   ┌───────────┐
     │ MenuItem  │    │ CartItem  │   │ MenuItem  │ (Shared live references)
     └───────────┘    └───────────┘   └───────────┘
```

While the code compiles, executes, and passes a basic happy path test, it suffers from severe architectural design weaknesses that make it fragile, bug-prone, and hard to maintain.

---

## 2. Class Responsibilities

| Class | Stated Purpose | Design Flaws |
| :--- | :--- | :--- |
| `FoodDeliveryMonolithManager` | System orchestrator | **God Class**: Owns state maps for restaurants, carts, orders, and drivers. Performs inline payment parsing, distance math, greedy dispatch loops, status changes, and console alerts. |
| `Restaurant` | Represents a dining establishment | Exposes internal mutable `Map<String, MenuItem>` directly. Uses raw `double` coordinates (primitive obsession). |
| `MenuItem` | Food item with price | All fields are `public` and mutable. Changing `price` mutates historical orders that reference this object. |
| `Cart` | Customer shopping cart | Public list of items. **Fails to enforce single-restaurant boundary**: permits items from different restaurants simultaneously. |
| `CartItem` | Item and quantity pair | Holds direct reference to mutable `MenuItem`. |
| `Order` | Order transaction | Holds live references to `CartItem`/`MenuItem` instead of immutable snapshots. Status is a free-form string with an unvalidated setter. |
| `DeliveryPartner` | Courier / driver | Public mutable state and raw coordinates. Status represented as a primitive boolean (`isAvailable`). |

---

## 3. Major Problems

1. **Retroactive Price Mutation**:
   `Order` contains live references to `MenuItem`. When a restaurant updates the price of a menu item, historical orders recalculate to the new price, corrupting past billing records.
2. **Cross-Restaurant Cart Contamination**:
   `Cart` has no concept of a restaurant boundary. Customers can add items from multiple restaurants into a single cart. When placed, an order is assigned to a single restaurant ID, stranding items from other restaurants.
3. **Unvalidated State Transitions**:
   `Order.setStatus(String)` allows arbitrary status changes. An order marked `DELIVERED` can be regressed back to `PREPARING` or `CANCELLED`.
4. **Primitive Obsession & Data Clumps**:
   Geographic coordinates (`lat`, `lon`) are passed repeatedly as raw `double` pairs rather than encapsulating them inside a cohesive `Location` abstraction with distance calculation methods.

---

## 4. SOLID Violations

- **Single Responsibility Principle (SRP)**:
  `FoodDeliveryMonolithManager` has at least 7 distinct reasons to change: restaurant management, cart rules, checkout workflow, payment gateway integration, delivery fee algorithms, driver dispatch heuristics, and notification channels.
- **Open/Closed Principle (OCP)**:
  Adding a new payment method (e.g., Apple Pay) requires modifying `placeOrder()` with another `else if` branch. Adding surge pricing or dynamic driver matching requires editing the manager class directly.
- **Liskov Substitution Principle (LSP)**:
  Not applicable due to the complete lack of inheritance and polymorphic abstractions.
- **Interface Segregation Principle (ISP)**:
  No interfaces exist; clients must depend on the concrete monolith.
- **Dependency Inversion Principle (DIP)**:
  High-level order placement logic directly depends on concrete details: hardcoded Euclidean distance formulas, hardcoded console printing (`System.out.println`), and inlined payment parsing.

---

## 5. Coupling Problems

- **Direct Memory Sharing**: `Order` shares live references to `MenuItem` objects in `Restaurant`. Mutating a restaurant's menu directly alters placed orders.
- **Tightly Coupled Notification**: The checkout and status update methods call `System.out.println` directly. Swapping to SMS, Email, or WebSockets requires rewriting domain business logic.
- **Hardcoded Payment Logic**: Payment gateway validation rules are directly inlined into the checkout method.

---

## 6. Testing Problems

- **Cannot Test Checkout in Isolation**: Testing order placement requires initializing restaurants, menu items, carts, customer coordinates, and payment strings inside the monolith.
- **Cannot Mock Payment Gateways**: Because payment processing is inlined with `if-else` blocks, third-party payment failures cannot be mocked or tested independently.
- **Console Output Verification**: Verifying notifications requires capturing standard system output streams.

---

## 7. Extension Problems

- Adding promo codes or surge delivery pricing requires modifying `placeOrder()` with more conditional statements.
- Changing delivery partner matching from greedy first-available to proximity-based or rating-based requires rewriting `assignDeliveryPartner()`.
- Supporting cash refunds or webhook callbacks requires invasive surgery across the monolithic manager.

---

## 8. What Should Change in Good Design

1. **Break up the God Class**: Delegate responsibilities to cohesive domain services (`RestaurantService`, `CartService`, `OrderService`, `DeliveryService`).
2. **Encapsulate Domain Rules**: Enforce single-restaurant boundary inside `Cart.addItem()`.
3. **Immutable Price Snapshots**: Snapshot item details (`menuItemId`, `name`, `unitPrice`, `quantity`) into an immutable `OrderItem` record when creating an `Order`.
4. **Guarded State Machine**: Enforce strict, valid transitions between `OrderStatus` states.
5. **Decouple Payments via Abstraction**: Introduce a `PaymentProcessor` interface to satisfy OCP and DIP.
6. **Encapsulate Value Objects**: Replace raw coordinate doubles with an immutable `Location` record.
