# 18. SOLID Principles

## 1. What is it?

**SOLID** is a mnemonic acronym for five foundational design principles formulated by Robert C. Martin ("Uncle Bob") for building maintainable, understandable, and flexible object-oriented software.

The five principles are:
* **S** — **Single Responsibility Principle (SRP)**
* **O** — **Open/Closed Principle (OCP)**
* **L** — **Liskov Substitution Principle (LSP)**
* **I** — **Interface Segregation Principle (ISP)**
* **D** — **Dependency Inversion Principle (DIP)**

SOLID serves as the primary compass for evaluating object-oriented architecture in Low-Level Design (LLD).

---

## 2. Why do we need it?

Without SOLID principles, software systems rapidly degenerate through three classic failure modes:
1. **Rigidity**: Hard to change because every change affects too many parts.
2. **Fragility**: Breaking unrelated features whenever a change is made elsewhere.
3. **Immobility**: Inability to reuse components in new projects due to entangled dependencies.

Applying SOLID creates modular code that gracefully accommodates new business requirements without constant refactoring.

---

## 3. How does it work? (The 5 Principles Broken Down)

### S — Single Responsibility Principle (SRP)
> *"A class should have one, and only one, reason to change."*
* A class should focus on one business actor or single concern. Do not mix database access, business rules, and presentation logic in the same class.

### O — Open/Closed Principle (OCP)
> *"Software entities should be open for extension, but closed for modification."*
* You should be able to add new functionality by **adding new classes** (via polymorphism or composition) rather than modifying existing, tested source code.

### L — Liskov Substitution Principle (LSP)
> *"Subtypes must be substitutable for their base types without altering system correctness."*
* If class B extends class A, passing B anywhere A is expected must not violate invariants, break contracts, or throw `UnsupportedOperationException`.

### I — Interface Segregation Principle (ISP)
> *"Clients should not be forced to depend on interfaces they do not use."*
* Prefer many small, focused interfaces over one giant "fat" interface.

### D — Dependency Inversion Principle (DIP)
> *"High-level modules should not depend on low-level modules; both should depend on abstractions."*
* Business services should depend on abstract interfaces (like `OrderRepository`), not concrete infrastructure implementations (like `PostgresOrderRepository`).

---

## 4. One Simple Real-World Analogy

Think of a **Stereo Sound System**:
* **SRP**: The Speaker plays sound; the Turntable reads vinyl; the Amplifier boosts wattage. None of them try to do the other's job.
* **OCP**: You can add a Bluetooth receiver without opening up and re-soldering the amplifier's internal circuitry.
* **LSP**: Any standard 3.5mm audio cable works interchangeably, whether it comes from an iPhone, an Android, or a guitar synthesizer.
* **ISP**: The headphone jack only exposes audio output, not power inputs or tuner dials.
* **DIP**: The amplifier connects to speakers via standard speaker-wire terminals (abstractions), rather than being permanently hardwired to one specific brand of speaker.

---

## 5. Small Java Code Example (Java 21)

Here is a concise food delivery discount and notification flow illustrating all 5 principles working together:

```java
package com.systemdesign.lld.fundamentals.solid;

// 1. OCP & Strategy: Open for new discount strategies, closed for modification
public interface DiscountStrategy {
    double applyDiscount(double rawAmount);
}

public class FlatDiscountStrategy implements DiscountStrategy {
    private final double discountAmount;
    public FlatDiscountStrategy(double discountAmount) { this.discountAmount = discountAmount; }

    @Override
    public double applyDiscount(double rawAmount) {
        return Math.max(0, rawAmount - discountAmount);
    }
}
```

```java
// 2. ISP: Clean, segregated interfaces instead of one massive Messenger
public interface Notifier {
    void sendNotification(String message);
}

// 3. DIP: High-level OrderCheckoutService depends on abstractions (Notifier, DiscountStrategy)
public class OrderCheckoutService {
    private final DiscountStrategy discountStrategy;
    private final Notifier notifier;

    // Dependency Injected via constructor
    public OrderCheckoutService(DiscountStrategy discountStrategy, Notifier notifier) {
        this.discountStrategy = discountStrategy;
        this.notifier = notifier;
    }

    // SRP: Exclusively handles checkout business logic
    public double checkout(String orderId, double originalPrice) {
        double finalPrice = discountStrategy.applyDiscount(originalPrice);
        System.out.println("Order " + orderId + " calculated: $" + finalPrice);
        notifier.sendNotification("Order " + orderId + " total is $" + finalPrice);
        return finalPrice;
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        // Concrete implementations plugged in at runtime
        DiscountStrategy promo = new FlatDiscountStrategy(20.0);
        Notifier emailNotifier = msg -> System.out.println("[EMAIL SENT] " + msg);

        OrderCheckoutService checkoutService = new OrderCheckoutService(promo, emailNotifier);
        checkoutService.checkout("ORD-101", 100.0); // Total: $80.0
    }
}
```

---

## 6. Diagram

```mermaid
flowchart TD
    subgraph SOLIDArchitecture["SOLID Architecture Flow"]
        direction TB
        Client[Client] --> S["OrderCheckoutService\n(SRP: Checkout logic only)"]
        
        S -->|DIP: depends on abstraction| DInt["<<interface>>\nDiscountStrategy\n(OCP: Extensible)"]
        S -->|DIP: depends on abstraction| NInt["<<interface>>\nNotifier\n(ISP: Lean interface)"]
        
        FDS["FlatDiscountStrategy\n(LSP: Substitutable)"] ..|> DInt
        PDS["PercentageDiscountStrategy\n(LSP: Substitutable)"] ..|> DInt
        
        EN["EmailNotifier"] ..|> NInt
        SN["SmsNotifier"] ..|> NInt
    end
```

### Explanation of Diagram
`OrderCheckoutService` adheres to SRP. It depends on `DiscountStrategy` and `Notifier` interfaces (DIP). New discount rules can be added without modifying the service (OCP). The interfaces are small and focused (ISP), and all concrete strategies are 100% interchangeable (LSP).

---

## 7. Advantages and Disadvantages

### Advantages
* **Extensibility**: Add new features by adding new classes without risking regressions in existing code.
* **Extreme Testability**: Every dependency can be cleanly mocked or faked during testing.
* **Maintainability**: Clear boundaries make enterprise codebases easy to understand for new engineers.

### Disadvantages
* **Interface Proliferation**: Inexperienced developers can create excessive layers of interfaces for trivial features.
* **Higher Initial Development Time**: Requires more planning than slapping code into a single file.

---

## 8. When to Use It and When Not to Use It

### When to Use It
* **Always** in production backend systems, domain architectures, and LLD interview designs.
* Whenever a codebase will be maintained by multiple engineers over months or years.

### When Not to Use It
* Throwaway prototypes, shell automation scripts, or hackathon proofs-of-concept where speed of initial delivery outweighs future maintainability.

---

## 9. Common Mistakes and Misconceptions

* **Mistake: Violating LSP with Square extending Rectangle**:
  Setting `setWidth()` on a square unexpectedly changes its height, violating the caller's expectations of a true rectangle.
* **Mistake: Treating SRP as "A class must have only one method"**:
  SRP means a class has one **conceptual responsibility or business actor**, not literally one single method.
* **Mistake: Creating massive "God Interfaces" (violating ISP)**:
  Creating an interface with 30 methods, forcing concrete classes to write dummy empty implementations for methods they don't care about.

---

## 10. Common LLD Interview Questions with Answers

### Q1: How do you identify an Open/Closed Principle (OCP) violation during an LLD interview?
**Answer**: Look for `switch` or `if/else` statements that inspect an object's type (e.g., `if (type == CREDIT_CARD) ... else if (type == UPI)`). Whenever a new type requires modifying this existing `switch` statement, OCP is violated. Fix it by replacing the condition with a polymorphic interface (the Strategy or Factory Pattern).

### Q2: What is the difference between Interface Segregation (ISP) and Single Responsibility (SRP)?
**Answer**:
* **SRP** is concerned with **implementation and cohesion**: a class should have one reason to change.
* **ISP** is concerned with **callers and contracts**: an interface should not force a client to depend on methods it does not invoke.

### Q3: What is the practical difference between Dependency Inversion (DIP) and Dependency Injection (DI)?
**Answer**:
* **DIP** is the **high-level architectural principle**: High-level modules should depend on abstractions (interfaces), not concrete classes.
* **DI** is the **concrete design pattern / implementation technique**: Supplying those abstractions to a dependent object from the outside (typically via constructor injection).

---

## 11. Short Revision Notes

* **S (SRP)**: 1 class = 1 reason to change.
* **O (OCP)**: Open for extension (new classes), closed for modification.
* **L (LSP)**: Subclasses must not break superclass contracts.
* **I (ISP)**: Lean, specialized interfaces > fat, bloated interfaces.
* **D (DIP)**: Depend on abstractions (interfaces), never on concrete implementations.
