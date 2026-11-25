# 03. Encapsulation

## 1. What is it?

**Encapsulation** is the practice of bundling data (fields) and the methods that operate on that data into a single cohesive unit (a class), while **restricting direct access** to the internal state from the outside world.

Encapsulation ensures that an object's internal representation can only be inspected or modified through a controlled, well-defined public interface. It is the primary mechanism for maintaining **class invariants** (rules that must always remain true throughout the lifetime of the object).

---

## 2. Why do we need it?

If fields in a class are declared `public` or exposed through careless setters:
* **Corrupted State**: Any external code can assign illegal or contradictory values (e.g., setting `order.total = -500.0` or `user.age = -15`).
* **Leaky Invariants**: Business rules (like "an account balance cannot drop below zero") cannot be enforced because clients bypass your logic and set fields directly.
* **Brittle Code**: If you change the internal data structure (e.g., changing a `List<String> items` to a `Map<String, Item>`), all external callers break immediately.

Encapsulation gives the class **complete authority** over its own integrity and isolates callers from internal design changes.

---

## 3. How does it work?

Encapsulation relies on three fundamental mechanisms:

1. **Access Modifiers**:
   - `private`: Visible only within the class itself (standard for instance variables).
   - `package-private` (default): Visible within the same package.
   - `protected`: Visible within the same package and subclasses.
   - `public`: Visible to all callers (reserved for the public API).
2. **Controlled Accessors and Mutators**: Expose meaningful domain methods (`deposit()`, `cancelOrder()`) rather than dumb field setters (`setBalance()`, `setStatus()`).
3. **Defensive Copying**: When returning or accepting mutable objects (like `Date` or `List`), return unmodifiable copies so outside callers cannot tamper with internal collections behind the scenes.

---

## 4. One Simple Real-World Analogy

Think of a **Capsule Pill** or an **ATM Machine**:
* An **ATM** holds hundreds of thousands of dollars inside a heavy vault.
* The bank does **not** let customers walk into the vault and grab cash by hand (unencapsulated).
* Instead, the ATM exposes a small public panel: a keypad, a screen, and a cash slot.
* When you request $100, the ATM validates your PIN, checks your account balance, verifies hardware bill levels, dispenses the exact cash, and records the audit log.
* The internal vault mechanics are completely private and protected from tampering.

---

## 5. Small Java Code Example (Java 21)

```java
package com.systemdesign.lld.fundamentals.encapsulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DigitalWallet {
    // 1. Private fields hide internal representation
    private final String walletId;
    private double balance;
    private final List<String> transactionHistory;

    public DigitalWallet(String walletId, double initialBalance) {
        if (walletId == null || walletId.isBlank()) {
            throw new IllegalArgumentException("Wallet ID cannot be blank");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.walletId = walletId;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add("Wallet opened with balance: " + initialBalance);
    }

    // 2. State modification exclusively through validated business methods
    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
        this.transactionHistory.add("Deposited: " + amount);
    }

    public void pay(double amount, String recipient) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (amount > this.balance) {
            throw new IllegalStateException("Insufficient funds for payment to: " + recipient);
        }
        this.balance -= amount;
        this.transactionHistory.add("Paid " + amount + " to " + recipient);
    }

    // 3. Controlled read-only access (no raw setters)
    public double getBalance() {
        return this.balance;
    }

    // 4. Defensive copying: prevent outside mutation of internal list
    public List<String> getTransactionHistory() {
        return Collections.unmodifiableList(this.transactionHistory);
    }
}
```

---

## 6. Diagram

```mermaid
flowchart TD
    subgraph Caller["Outside World / Client Code"]
        C[Client]
    end

    subgraph ObjectBoundary["Encapsulated DigitalWallet Object"]
        direction TB
        API["Public Interface\n- addFunds(amount)\n- pay(amount, recipient)\n- getBalance()"]
        
        subgraph InternalHidden["Private State (Protected Invariants)"]
            F1["walletId"]
            F2["balance"]
            F3["transactionHistory"]
        end
        
        API -->|Validates & Mutates| InternalHidden
    end

    C -->|Calls allowed methods| API
    C -.x|Direct access blocked| InternalHidden
```

### Explanation of Diagram
The client can only reach the public API methods (`addFunds`, `pay`). The internal fields (`balance`, `transactionHistory`) are shielded behind private access, ensuring validation logic always runs before any field value updates.

---

## 7. Advantages and Disadvantages

### Advantages
* **Guaranteed Invariants**: An object cannot enter an invalid or corrupted state.
* **Flexibility of Implementation**: You can refactor private fields (e.g., caching a computed total or changing a data type) without altering the calling code.
* **Side-Effect Control**: You can trigger audits, metrics, or validation whenever state changes.
* **Reduced Cognitive Load**: Callers only need to understand the clean public interface, not hundreds of lines of internal state logic.

### Disadvantages
* **Slight Boilerplate**: Requires writing explicit validation methods, constructors, and defensive copies.
* **Performance Consideration**: Returning unmodifiable wrappers or defensive copies creates extra lightweight wrapper objects.

---

## 8. When to Use It and When Not to Use It

### When to Use It
* **Always by default** for all domain models, business logic classes, entities, and aggregates in LLD.
* Whenever multiple fields have interdependent rules (e.g., `startDate` must be before `endDate`).

### When Not to Use It
* Internal, private static data structures inside a single algorithm where performance is strictly critical and scope is under 20 lines of code.
* Pure Java `record` types acting as inert data transfer objects (DTOs), where fields are inherently `final` and transparent by design.

---

## 9. Common Mistakes and Misconceptions

* **Misconception: "Encapsulation = Private variables + Getters and Setters"**:
  Generating a public getter and public setter for every single private variable defeats the entire purpose of encapsulation. That is merely public variables with extra syntax! Real encapsulation exposes **domain operations** (`withdraw()`, `confirmOrder()`), not blind field mutations (`setBalance()`, `setStatus()`).
* **Mistake: Leaking internal references**:
  Returning a direct reference to a mutable internal collection (e.g., `public List<Item> getItems() { return this.items; }`) allows callers to run `getItems().clear()` and wipe out the object's state without the object knowing. Always use `Collections.unmodifiableList(...)` or defensive copies.
* **Mistake: Validating only in the constructor**:
  If mutator methods exist, they must enforce the same invariant checks that the constructor does.

---

## 10. Common LLD Interview Questions with Answers

### Q1: What is the difference between Encapsulation and Abstraction?
**Answer**:
* **Encapsulation** is about **protection and containment**: bundling data with code and hiding internal mechanics to protect object integrity (`private` fields, defensive copying).
* **Abstraction** is about **simplification and contracts**: hiding complexity by exposing high-level capabilities without revealing how they are implemented (`interface`, `abstract class`).
* *Summary*: Encapsulation hides **data** (implementation details); Abstraction hides **complexity** (interface vs implementation).

### Q2: What is "Defensive Copying" and why is it vital for encapsulation?
**Answer**: Defensive copying is the technique of making a copy of a mutable object before storing it in a private field or returning it from an accessor. Without defensive copying, an external caller holds a reference to the private internal object and can mutate it directly outside the class's control, violating encapsulation.

### Q3: Why is "Tell, Don't Ask" considered a fundamental rule of encapsulation?
**Answer**: "Tell, Don't Ask" states that instead of asking an object for its state, making decisions outside, and setting the state back, you should **tell** the object what action to perform. For example, instead of `if (wallet.getBalance() >= 50) wallet.setBalance(wallet.getBalance() - 50);`, you tell the object `wallet.pay(50);`. This keeps business logic inside the object where the data lives.

---

## 11. Short Revision Notes

* **Core Rule**: Keep fields `private`. Expose business methods, not indiscriminate getters/setters.
* **Protect Invariants**: Validate every input in constructors and mutator methods.
* **Never Leak Mutable Objects**: Return unmodifiable collections (`Collections.unmodifiableList`) or defensive copies.
* **Principle to Remember**: **"Tell, Don't Ask"** — let objects manage their own decisions.
