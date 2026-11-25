# 16. Coupling

## 1. What is it?

**Coupling** is the degree of interdependence and interconnectedness between software modules, classes, or components. It measures **how closely connected two classes are**, and how much one class knows about or relies on the internal implementation of another.

* **Tight Coupling (High Coupling)**: Classes are heavily dependent on each other's concrete types, internal variables, or specific implementations. Changing one class forces cascading modifications across many other classes.
* **Loose Coupling (Low Coupling)**: Classes interact through lean, stable abstractions (interfaces). Changes to internal implementation details of one class have zero impact on other classes.

In software engineering, the universal architectural goal is **Loose Coupling**.

---

## 2. Why do we need it?

In systems with tight coupling:
* **The "Shotgun Surgery" Code Smell**: Modifying a simple requirement forces you to touch 15 different files.
* **Untestable Code**: You cannot unit-test a class in isolation because it directly instantiates real databases, network sockets, or file systems.
* **Zero Reusability**: You cannot pick up a class and reuse it in another project because it drags half the codebase with it.

Minimizing coupling ensures modularity, testability, and parallel development across teams.

---

## 3. How does it work? (Types of Coupling from Worst to Best)

Coupling exists on a spectrum:

1. **Content Coupling (Worst)**: One class directly modifies private fields or relies on internal branch mechanics of another class (breaks encapsulation completely).
2. **Common Coupling**: Multiple classes read and write to the same shared global variable or mutable singleton.
3. **Control Coupling**: One class passes a control flag (like a boolean `isVip` or integer mode) to dictate the internal control flow of another class.
4. **Stamp (Data-Structure) Coupling**: A class passes a large composite object (e.g., the entire `Customer` object) to a method that only needs a single primitive value (e.g., `zipCode`).
5. **Data Coupling**: Classes communicate purely by passing minimal primitive data arguments.
6. **Message / Interface Coupling (Best)**: Classes communicate exclusively through abstract contracts (interfaces).

---

## 4. One Simple Real-World Analogy

Think of **Connecting Electronics**:
* **Tight Coupling**: An old bedside lamp where the electrical cord is soldered directly into the wiring inside your house's drywall. If you want to replace the lamp with a different model, you must cut into the wall and hire an electrician.
* **Loose Coupling**: A standard electrical wall socket.
  - The wall provides a standard two-prong or three-prong plug interface.
  - You can plug in a lamp, a vacuum cleaner, a phone charger, or a laptop.
  - Upgrading the lamp requires zero changes to the house's electrical panel.

---

## 5. Small Java Code Example (Java 21)

### Tight Coupling (Anti-Pattern)
```java
// TIGHT COUPLING: Hardwired to concrete MySQL client and SMTP sender
public class BadOrderProcessor {
    private MySqlDatabase database = new MySqlDatabase(); // Hard dependency
    private SmtpEmailClient emailClient = new SmtpEmailClient(); // Hard dependency

    public void processOrder(String orderId) {
        database.save(orderId);
        emailClient.send("Order confirmed");
    }
}
```

### Loose Coupling (Preferred Pattern)
```java
package com.systemdesign.lld.fundamentals.coupling;

// 1. Stable abstractions
public interface OrderRepository {
    void save(String orderId);
}

public interface MessageNotifier {
    void sendNotification(String message);
}
```

```java
// 2. High-level class is loosely coupled via interfaces
public class OrderProcessor {
    private final OrderRepository repository;
    private final MessageNotifier notifier;

    // Injected via constructor - zero concrete coupling!
    public OrderProcessor(OrderRepository repository, MessageNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public void processOrder(String orderId) {
        System.out.println("Processing order: " + orderId);
        repository.save(orderId);
        notifier.sendNotification("Order " + orderId + " confirmed.");
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        // Concrete implementations can be anything (DB, In-Memory, Mock)
        OrderRepository repo = id -> System.out.println("Saved " + id + " to database.");
        MessageNotifier notifier = msg -> System.out.println("Notification sent: " + msg);

        OrderProcessor processor = new OrderProcessor(repo, notifier);
        processor.processOrder("ORD-4001");
    }
}
```

---

## 6. Diagram

```mermaid
flowchart LR
    subgraph TightCoupling["Tight Coupling (Brittle)"]
        direction TB
        C1[OrderProcessor] -->|new MySqlDatabase| D1[MySqlDatabase]
        C1 -->|new SmtpClient| E1[SmtpClient]
    end

    subgraph LooseCoupling["Loose Coupling (Flexible)"]
        direction TB
        C2[OrderProcessor] -->|depends on| I1["<<interface>>\nOrderRepository"]
        C2 -->|depends on| I2["<<interface>>\nMessageNotifier"]
        M1[MySqlRepo] ..|> I1
        M2[MongoRepo] ..|> I1
        S1[EmailNotifier] ..|> I2
        S2[SmsNotifier] ..|> I2
    end
```

### Explanation of Diagram
In the tightly coupled diagram, `OrderProcessor` directly instantiates concrete classes, making it impossible to change technologies or mock dependencies. In the loosely coupled design, `OrderProcessor` depends on interfaces, allowing any database or notification provider to be plugged in at runtime.

---

## 7. Advantages and Disadvantages

### Advantages of Loose Coupling
* **Independent Evolvability**: You can replace or upgrade one component without touching callers.
* **Effortless Unit Testing**: Dependencies can be instantly mocked using standard mocking frameworks or simple lambdas.
* **Parallel Team Velocity**: Teams can work simultaneously against agreed-upon interfaces without waiting for concrete backends.

### Disadvantages of Loose Coupling
* **Extra Indirection**: Navigating through interfaces adds extra files and slight mental overhead during initial code browsing.
* **Risk of Over-Abstraction**: Creating interfaces for classes that will genuinely only ever have one single implementation ("interfaceitis").

---

## 8. When to Use It and When Not to Use It

### Strive for Loose Coupling:
* Between distinct architectural layers (Controller $\to$ Service $\to$ Repository).
* At external boundaries (third-party payment gateways, message queues, external microservices).
* In all Low-Level Design interview solutions.

### Tolerate Tighter Coupling:
* Within a tightly cohesive private module or package (e.g., an `Order` and its private `OrderLineItem`s).
* Between pure primitive utility functions that have zero state (e.g., `Math.max()`).

---

## 9. Common Mistakes and Misconceptions

* **Misconception: Loose coupling means zero coupling**: Zero coupling means your classes do not communicate at all, which produces a useless application. The goal is **loose, well-designed coupling through contracts**.
* **Mistake: Instantiating concrete dependencies with `new` inside business classes**:
  Using `new ConcreteService()` inside a business method immediately introduces tight coupling. Always inject dependencies.
* **Mistake: Leaking internal data structures**:
  Accepting or returning implementation-specific collections (like `java.util.concurrent.ConcurrentHashMap`) instead of `java.util.Map`.

---

## 10. Common LLD Interview Questions with Answers

### Q1: What is the relationship between Coupling and Cohesion?
**Answer**: They are complementary design goals. The universal rule is **"High Cohesion, Low Coupling"**. High cohesion means a class focuses strictly on doing one job well. Low coupling means the class has minimal dependencies on the internal implementation of outside classes.

### Q2: What are Afferent Coupling (Ca) and Efferent Coupling (Ce)?
**Answer**:
* **Afferent Coupling (Ca) / Incoming**: The number of other classes that depend *on* this class (measures responsibility/importance).
* **Efferent Coupling (Ce) / Outgoing**: The number of other classes that this class depends *upon* (measures vulnerability/instability).
* A class with high Ce and low Ca is unstable; a class with high Ca and zero Ce (like an interface) is maximally stable.

### Q3: How does the Dependency Inversion Principle (DIP) promote loose coupling?
**Answer**: DIP states that high-level modules should not depend on low-level modules; both should depend on abstractions. By placing an interface between the caller and the callee, the caller is completely decoupled from the concrete implementation details.

---

## 11. Short Revision Notes

* **Definition**: Degree of interdependence between classes.
* **Target**: **Low / Loose Coupling**.
* **Primary Mechanism**: Program to interfaces, use Dependency Injection.
* **Red Flag**: The `new` keyword instantiating heavy services inside business logic.
