# 28. Dependency Injection (DI)

## 1. What is it?

**Dependency Injection (DI)** is a software design pattern implementing **Inversion of Control (IoC)** where an object receives its dependencies from an external assembler or coordinator, rather than creating them itself internally using the `new` operator.

A **dependency** is any service, repository, or collaborator that an object needs to perform its job. With Dependency Injection, the responsibility of instantiating and wiring objects is inverted:
* Instead of the class saying: *"I will instantiate my own MySQL database client"*,
* The class says: *"I require a `Database` interface; someone give it to me when I am constructed."*

---

## 2. Why do we need it?

When a class instantiates its own dependencies directly:
```java
// TIGHT COUPLING: Hardwired dependency
public class OrderService {
    private PaymentGateway gateway = new StripePaymentGateway(); // Hardwired!
}
```
* **Impossible to Unit Test**: You cannot test `OrderService` without actually charging real credit cards on Stripe's live servers!
* **Violates Dependency Inversion Principle (DIP)**: High-level business logic is directly coupled to low-level third-party vendor libraries.
* **Rigid Architecture**: Swapping Stripe for PayPal requires modifying and recompiling core business services.

Dependency Injection completely decouples class behavior from dependency creation.

---

## 3. How does it work?

Dependency Injection can be achieved in three ways:

1. **Constructor Injection (The Industry Standard)**:
   Dependencies are provided as arguments to the class constructor.
2. **Setter Injection**:
   Dependencies are injected via public setter methods (useful only for optional dependencies).
3. **Field Injection (Anti-Pattern)**:
   Dependencies are injected directly into private fields using reflection (e.g., `@Autowired` on private fields without constructors).

```
┌────────────────────────────────────────────────────────┐
│                   EXTERNAL ASSEMBLER                   │
│         (DI Container / Main method / Config)          │
│                                                        │
│  1. Instantiates StripeGateway                         │
│  2. Instantiates PostgresRepo                          │
│  3. Injects them into OrderService constructor         │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼ creates & passes
┌────────────────────────────────────────────────────────┐
│                      OrderService                      │
│        (Has no knowledge of how dependencies were      │
│         created; depends purely on interfaces)         │
└────────────────────────────────────────────────────────┘
```

---

## 4. One Simple Real-World Analogy

Think of an **Actor on a Movie Set**:
* In a movie scene, an actor plays the role of a knight.
* The actor does **not** mine iron ore, forge a sword, or sew leather armor in their trailer before walking onto the set.
* Instead, the **Prop Department (The DI Injector)** hands the sword, shield, and armor to the actor.
* If tomorrow's scene takes place in outer space, the same prop department hands the actor a laser blaster and a spacesuit. The actor focuses purely on acting (business logic).

---

## 5. Small Java Code Example (Java 21)

This example demonstrates pure Dependency Injection **without any framework** (Pure Java):

```java
package com.systemdesign.lld.fundamentals.dependencyinjection;

// 1. Abstraction interfaces
public interface PaymentGateway {
    void process(double amount);
}

public interface OrderRepository {
    void save(String orderId, double amount);
}
```

```java
// 2. High-level service: Dependencies are INJECTED via constructor
public class OrderService {
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;

    // DEPENDENCY INJECTION: Class receives its dependencies from outside
    public OrderService(PaymentGateway paymentGateway, OrderRepository orderRepository) {
        this.paymentGateway = paymentGateway;
        this.orderRepository = orderRepository;
    }

    public void placeOrder(String orderId, double amount) {
        System.out.println("Placing order: " + orderId);
        paymentGateway.process(amount);
        orderRepository.save(orderId, amount);
    }
}
```

```java
// 3. The Assembler / Main: Injects real implementations or mock test doubles
public class Main {
    public static void main(String[] args) {
        // PRODUCTION SETUP: Wire real implementations
        PaymentGateway realGateway = amt -> System.out.println("Charged $" + amt + " via Stripe");
        OrderRepository realRepo = (id, amt) -> System.out.println("Saved order " + id + " to Postgres");

        OrderService prodService = new OrderService(realGateway, realRepo);
        prodService.placeOrder("ORD-101", 89.99);

        // TEST SETUP: Wire mock stubs for lightning-fast, isolated unit testing!
        PaymentGateway mockGateway = amt -> System.out.println("[TEST MOCK] Payment verified");
        OrderRepository mockRepo = (id, amt) -> System.out.println("[TEST MOCK] Order recorded in-memory");

        OrderService testService = new OrderService(mockGateway, mockRepo);
        testService.placeOrder("ORD-TEST", 10.0);
    }
}
```

---

## 6. Diagram

```mermaid
flowchart TD
    subgraph WithoutDI["Without Dependency Injection (Hardwired)"]
        direction TB
        OS1[OrderService] -->|new| SG1[StripePaymentGateway]
        OS1 -->|new| PR1[PostgresRepository]
        Note1["Cannot mock. Cannot swap.\nTightly coupled."]
    end

    subgraph WithDI["With Dependency Injection (IoC Container / Assembler)"]
        direction TB
        Injector["Assembler / DI Container"]
        OS2[OrderService]
        PGInt["<<interface>>\nPaymentGateway"]
        RepoInt["<<interface>>\nOrderRepository"]
        
        Injector -->|creates & injects| OS2
        Injector -->|creates| PG1[StripeGateway]
        Injector -->|creates| DB1[PostgresRepo]
        
        OS2 --> PGInt
        OS2 --> RepoInt
        PG1 ..|> PGInt
        DB1 ..|> RepoInt
    end
```

### Explanation of Diagram
Without DI, `OrderService` controls the instantiation of concrete dependencies. With DI, an external Assembler/Container creates the concrete objects and injects them into `OrderService`, which depends strictly on abstract interfaces.

---

## 7. Advantages and Disadvantages

### Advantages
* **Effortless Unit Testing**: Swap heavy network dependencies with in-memory test mocks in milliseconds.
* **Loose Coupling**: Business classes know nothing about how dependencies are configured or constructed.
* **Single Configuration Root**: Dependency graph wiring is consolidated in one place (e.g., a Spring `@Configuration` class or `main` method).

### Disadvantages
* **Slight Traceability Complexity**: Finding which concrete class is being injected at runtime requires checking configuration files or IDE navigation tools.
* **Container Learning Curve**: Enterprise frameworks like Spring can feel like "magic" to beginners if underlying mechanics aren't understood.

---

## 8. When to Use It and When Not to Use It

### Always Use Dependency Injection For:
* Domain business services, controllers, repositories, and workflow orchestrators.
* External infrastructure collaborators (clients, drivers, message queues).

### Do NOT Use Dependency Injection For:
* Value Objects (`Money`, `GeoPoint`, `DateRange`) — these are lightweight, immutable data structures created via `new`.
* Standard Java library utilities (e.g., `Math.max()`, `Collections.sort()`).

---

## 9. Common Mistakes and Misconceptions

* **Misconception: "Dependency Injection requires Spring Framework"**:
  False! Dependency Injection is an architectural design pattern, not a framework. You can implement DI in pure vanilla Java by passing objects into constructors (often called *Pure DI* or *Manual DI*).
* **Mistake: Using Field Injection (`@Autowired` on private fields)**:
  Hides dependencies, breaks immutability (`final` cannot be used), and makes running unit tests impossible without spinning up heavy reflection frameworks. Always prefer **Constructor Injection**.

---

## 10. Common LLD Interview Questions with Answers

### Q1: What is the difference between Inversion of Control (IoC) and Dependency Injection (DI)?
**Answer**:
* **IoC** is the broad **design principle**: Inverting the control of program execution (e.g., a framework calls your code, rather than your code calling a library).
* **DI** is the specific **design pattern / implementation** of IoC: Inverting the creation and management of an object's dependencies by providing them from the outside.

### Q2: What are the three primary types of Dependency Injection and which is preferred?
**Answer**:
1. **Constructor Injection (Preferred)**: Guarantees complete initialization, allows `final` immutable fields, and fails fast if dependencies are missing.
2. **Setter Injection**: Useful only for optional or reconfigurable dependencies with sensible defaults.
3. **Field Injection (Avoid)**: Uses reflection to inject private fields; violates OOP encapsulation and makes unit testing difficult.

### Q3: How does Dependency Injection prevent circular dependencies?
**Answer**: In constructor injection, if Class A requires Class B in its constructor, and Class B requires Class A, the JVM/framework cannot instantiate either object and throws a circular dependency error at startup (`BeanCurrentlyInCreationException`). This immediately alerts the engineer to an architectural design flaw (which can be resolved by extracting a shared interface, introducing an event/observer, or using a mediator).

---

## 11. Short Revision Notes

* **Essence**: Hand dependencies to objects; do not let objects instantiate their own collaborators.
* **Best Style**: **Constructor Injection** with `private final` fields.
* **Primary Benefit**: **Effortless Unit Testing** using mocks.
* **Remember**: DI is a clean Java pattern, not something exclusive to frameworks.
