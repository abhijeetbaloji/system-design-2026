# 07. Composition vs Inheritance

## 1. What is it?

**Composition vs Inheritance** is one of the most fundamental debates in object-oriented software architecture. It compares two primary mechanisms for code reuse and system structuring:

* **Inheritance ("is-a")**: A class extends another class, inheriting all its non-private fields and methods at compile time (`class Sub extends Super`).
* **Composition ("has-a")**: A class contains references to one or more objects of other classes as fields and delegates work to them (`class Order { private PaymentProcessor processor; }`).

The classic Gang of Four (GoF) design principle states:
> *"Favor object composition over class inheritance."*

---

## 2. Why do we need it?

Inheritance is often the first tool taught in OOP, leading developers to overuse it. Overusing inheritance introduces severe problems:

* **The Fragile Base Class Problem**: Changing a single method in a base class can silently break subclasses across the codebase.
* **Class Hierarchy Explosion**: If you use inheritance to combine multiple orthogonal features (e.g., `Car`, `ElectricCar`, `FlyingCar`, `ElectricFlyingCar`), the number of classes multiplies exponentially.
* **Compile-Time Rigidity**: An object's superclass is permanently locked in at compile time; you cannot swap an object's superclass while the program is running.
* **White-Box Reuse**: Inheritance exposes parent internal implementation details to the child, weakening encapsulation.

Composition solves these issues by treating components as interchangeable black boxes connected via clean interfaces.

---

## 3. How does it work?

| Dimension | Inheritance (`extends`) | Composition (`has-a`) |
|---|---|---|
| **Relationship** | "is-a" (e.g., `Dog is-a Animal`) | "has-a" (e.g., `Car has-a Engine`) |
| **Binding Time** | Compile-time (static) | Runtime (dynamic) |
| **Encapsulation** | White-box (breaks encapsulation) | Black-box (respects encapsulation) |
| **Flexibility** | Rigid, cannot change parent at runtime | Highly flexible, can swap components dynamically |
| **Coupling** | Tight coupling to superclass | Loose coupling to interfaces |

With composition, instead of saying *"A Fighter Jet is a Jet, which is an Aircraft, which is a FlyingObject"*, you say *"An Aircraft has a `FlightBehavior` and has a `WeaponSystem`"*. You can change its weapon system from missiles to lasers at runtime without creating a new subclass.

---

## 4. One Simple Real-World Analogy

Think of **Customizing a Desktop PC vs Buying a Sealed Unibody Laptop**:
* **Inheritance** is like a **sealed unibody laptop**: If you want a laptop with an RTX 4090 GPU, you must buy a dedicated sub-model. If you later want more RAM, you cannot swap the RAM chip because it is soldered onto the motherboard at the factory (compile-time binding).
* **Composition** is like a **custom gaming desktop**: The motherboard has standard PCI-e and RAM slots. You plug in an AMD or Nvidia graphics card. If you want a faster GPU next year, you simply unplug the old card and insert the new one without rebuilding the entire PC.

---

## 5. Small Java Code Example (Java 21)

### Problematic Approach: Class Explosion with Inheritance
```java
// Fragile & explodes if we have multiple movement types and attack types
class Monster { void move() {} }
class FlyingMonster extends Monster { /* ... */ }
class WalkingMonster extends Monster { /* ... */ }
class FlyingFireBreathingMonster extends FlyingMonster { /* ... */ }
// What if a monster walks AND breathes fire? Class explosion!
```

### Preferred Approach: Flexible Composition
```java
package com.systemdesign.lld.fundamentals.compositionvsinheritance;

// 1. Define behavioral strategies
public interface AttackStrategy {
    void attack();
}

public class FireBreathAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("Breathing scorching fire!");
    }
}

public class ClawAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("Slashing with razor-sharp claws!");
    }
}
```

```java
// 2. Monster COMPOSED OF an AttackStrategy (has-a)
public class Monster {
    private final String name;
    private AttackStrategy attackStrategy; // Composed dependency

    public Monster(String name, AttackStrategy attackStrategy) {
        this.name = name;
        this.attackStrategy = attackStrategy;
    }

    public void performAttack() {
        System.out.print(name + " attacks: ");
        attackStrategy.attack(); // Delegating behavior
    }

    // Dynamic runtime flexibility: swap behavior on the fly!
    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        // Monster starts with Claw attack
        Monster dragon = new Monster("Red Dragon", new ClawAttack());
        dragon.performAttack(); // Red Dragon attacks: Slashing with razor-sharp claws!

        // Swap behavior dynamically at runtime without subclassing
        dragon.setAttackStrategy(new FireBreathAttack());
        dragon.performAttack(); // Red Dragon attacks: Breathing scorching fire!
    }
}
```

---

## 6. Diagram

```mermaid
flowchart TD
    subgraph InheritanceWay["Inheritance: Rigid Class Explosion"]
        direction TB
        M[Monster]
        FM[FlyingMonster]
        WM[WalkingMonster]
        FFM["FlyingFireMonster\n(combining requires endless new classes)"]
        M --> FM
        M --> WM
        FM --> FFM
    end

    subgraph CompositionWay["Composition: Modular & Swappable"]
        direction LR
        Mon["Monster\n(holds reference)"]
        Strategy["<<interface>>\nAttackStrategy"]
        C1["ClawAttack"]
        C2["FireBreathAttack"]
        
        Mon -->|has-a| Strategy
        C1 ..|> Strategy
        C2 ..|> Strategy
    end
```

### Explanation of Diagram
On the left, inheritance forces the creation of a new class for every permutation of capabilities. On the right, composition models behavior as swappable strategy components, allowing any combination to be configured dynamically.

---

## 7. Advantages and Disadvantages

### Composition Advantages
* **Runtime Adaptability**: Behaviors can be swapped dynamically via setters or constructors.
* **Encapsulation Preserved**: Internal state of component classes remains completely private.
* **Zero Hierarchy Explosion**: Components combine multiplicatively without subclass proliferation.
* **High Testability**: You can easily inject mocks for composed dependencies.

### Composition Disadvantages
* **Delegation Boilerplate**: May require forwarding methods to delegate calls down to internal components.
* **Slight Object Overhead**: Creates more small objects in memory compared to a single monolithic subclass.

---

## 8. When to Use It and When Not to Use It

### When to Use Composition
* **Almost always by default** when designing business domains and system components.
* When behaviors need to vary independently or be configured at runtime.
* When reusing functionality from an unrelated class (e.g., logging, validation, notification).

### When to Use Inheritance
* When there is a **strict, permanent "is-a" relationship** that never changes over time.
* When the subclass satisfies the **Liskov Substitution Principle (LSP)** 100% without hacking or throwing `UnsupportedOperationException`.
* When writing framework base classes using the Template Method pattern (e.g., `HttpServlet.doGet()`).

---

## 9. Common Mistakes and Misconceptions

* **Mistake: Using inheritance just to save typing a few lines**:
  Inheriting from `ArrayList` just to get a `.add()` method exposes dozens of unwanted list methods on your class. Use composition and expose only the methods you need.
* **Mistake: Never using inheritance at all**:
  Inheritance is not "evil". Shallow (1-level) inheritance for polymorphic taxonomy with genuine subtyping is clean and appropriate.
* **Mistake: Forgetting delegation**:
  Composing an object but still making callers directly navigate its private internals (`order.getPayment().getGateway().charge()`) violates the Law of Demeter.

---

## 10. Common LLD Interview Questions with Answers

### Q1: Why does the Gang of Four (GoF) advise "Favor object composition over class inheritance"?
**Answer**: Because inheritance introduces tight compile-time coupling, exposes base class implementation details (white-box reuse), and causes the fragile base class problem. Composition provides loose coupling, black-box reuse, preserves encapsulation, and allows components to be swapped dynamically at runtime.

### Q2: What is an example in the standard Java library where inheritance was misused instead of composition?
**Answer**:
* `java.util.Stack extends Vector`: A `Stack` should only permit LIFO operations (`push`, `pop`, `peek`). Because it inherited from `Vector`, clients can call `stack.insertElementAt(0, "item")` or `stack.remove(2)`, violating the fundamental stack invariant. It should have composed a `List` internally instead.
* `java.util.Properties extends Hashtable<Object, Object>`: Allows non-string keys to be put into properties, violating its own domain contract.

### Q3: How do you refactor an inheritance hierarchy into composition?
**Answer**:
1. Identify the varying behavior in the superclass.
2. Extract that behavior into a dedicated interface.
3. Replace the `extends` clause in the subclass with a private field of that interface type.
4. Inject the interface implementation via constructor and delegate the calls.

---

## 11. Short Revision Notes

* **Inheritance** = "is-a" = Rigid = Compile-time = White-box reuse.
* **Composition** = "has-a" = Flexible = Runtime = Black-box reuse.
* **Golden Rule**: If a class merely *uses* another class's behavior, compose it; do not inherit it.
* **Classic Danger**: Beware of class explosion and the fragile base class problem.
