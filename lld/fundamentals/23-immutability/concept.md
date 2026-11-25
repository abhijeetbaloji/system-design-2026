# 23. Immutability

## 1. What is it?

An **immutable object** is an object whose internal state **cannot be modified** after it is constructed. Once an instance is created in memory, its values remain permanently fixed for the entire duration of its lifetime.

If you need to change the state of an immutable object, you do **not** mutate its existing fields; instead, you construct and return a **new instance** containing the updated values (copy-on-write).

In modern Java, immutability is natively championed by `java.lang.String`, primitive wrappers (`Integer`, `Double`), the `java.time` package (`LocalDate`, `Instant`), and Java **`record`** types.

---

## 2. Why do we need it?

Mutable shared state is the leading cause of critical bugs in software engineering:
* **Thread-Safety Hazards**: When multiple concurrent threads read and mutate the same object simultaneously, race conditions, memory visibility bugs, and corrupted states occur.
* **Accidental Side Effects**: If Function A passes an object to Function B, Function B might mutate internal fields without Function A's knowledge or consent.
* **Hash Corruption**: If an object is used as a key in a `HashMap` or `HashSet`, and its fields are mutated later, its hash code changes, making the object permanently lost inside the map!

Immutability eliminates these problems completely: **an object that cannot change cannot be corrupted**.

---

## 3. How does it work? (Rules for Creating Immutable Classes)

To create an immutable class in Java, follow these 5 golden rules:

1. **Do not provide setters** or any methods that modify state.
2. **Make all fields `private` and `final`**.
3. **Prevent subclassing**: Mark the class `final` (or make all constructors private and use static factories).
4. **Deep Immutability vs Shallow Immutability**:
   - Marking a collection `final List<String> list` is **shallow immutability** (the reference cannot change, but callers can still call `list.add("item")`!).
   - **Deep Immutability** requires wrapping collections in `Collections.unmodifiableList(...)` or `List.copyOf(...)`.
5. **Perform Defensive Copying**: Make defensive copies in constructors when receiving mutable objects, and in getters when returning them.

---

## 4. One Simple Real-World Analogy

Think of a **Printed Currency Note (\$100 Bill)**:
* Once the government mint prints a \$100 bill, its value and serial number are permanently immutable.
* You cannot take an eraser, wipe out the "100", write "50", and spend it at a store.
* If you want to pay \$50 with your \$100 bill, you hand it over, and you receive a **new** \$50 bill in change.
* The original bill was never mutated; new objects were exchanged.

---

## 5. Small Java Code Example (Java 21)

### Modern Approach: Using Java 21 `record`
```java
package com.systemdesign.lld.fundamentals.immutability;

import java.util.List;

// Records in Java are transparent, shallowly immutable by default
public record GeoPoint(double latitude, double longitude) {
    public GeoPoint {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid GPS coordinates");
        }
    }
}
```

### Deeply Immutable Class with Defensive Copying
```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class UserProfile { // 1. final class prevents extension
    private final String userId; // 2. private final fields
    private final List<String> permissions;

    public UserProfile(String userId, List<String> permissions) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID required");
        }
        this.userId = userId;
        // 3. Defensive Copying on ingestion to achieve DEEP immutability
        this.permissions = List.copyOf(permissions != null ? permissions : List.of());
    }

    // 4. Mutation creates and returns a NEW instance
    public UserProfile withAddedPermission(String newPermission) {
        List<String> updated = new ArrayList<>(this.permissions);
        updated.add(newPermission);
        return new UserProfile(this.userId, updated);
    }

    public String getUserId() { return userId; }
    
    // 5. Unmodifiable view prevents tampering
    public List<String> getPermissions() { return permissions; }
}
```

```java
public class Main {
    public static void main(String[] args) {
        List<String> initialRoles = new ArrayList<>();
        initialRoles.add("READ_CATALOG");

        UserProfile original = new UserProfile("USR-1", initialRoles);

        // Outside tampering does not affect the profile!
        initialRoles.add("DELETE_EVERYTHING");
        System.out.println("Original permissions: " + original.getPermissions()); // [READ_CATALOG]

        // Mutation returns a brand new instance
        UserProfile updated = original.withAddedPermission("WRITE_REVIEW");
        System.out.println("Updated permissions: " + updated.getPermissions());   // [READ_CATALOG, WRITE_REVIEW]
    }
}
```

---

## 6. Diagram

```mermaid
flowchart TD
    subgraph MutableMutation["Mutable Object (Hazardous)"]
        direction TB
        M1["Account (balance=100)"] -->|deposit(50)| M1Mutated["Same Account in Heap\n(mutated to balance=150)"]
        T1[Thread 1] --> M1Mutated
        T2[Thread 2] --> M1Mutated
        Note1["Race conditions & locks required!"]
    end

    subgraph ImmutableCopy["Immutable Object (Thread-Safe)"]
        direction TB
        I1["GeoPoint (lat=12.0, lon=77.0)"] -->|withLatitude(13.0)| I2["NEW GeoPoint in Heap\n(lat=13.0, lon=77.0)"]
        T3[Thread 1 reads I1]
        T4[Thread 2 reads I2]
        Note2["Zero locking needed. 100% thread safe!"]
    end
```

### Explanation of Diagram
In mutable objects, multiple threads compete to mutate the exact same heap memory cell, necessitating locks. In immutable objects, operations return new objects; references never see unexpected mutation, making them inherently thread-safe.

---

## 7. Advantages and Disadvantages

### Advantages
* **Inherently Thread-Safe**: No synchronization, mutexes, or locks are ever required for read-only concurrent access.
* **Safe Sharing**: Can be shared across threads, caches, and services without fear of data corruption.
* **Safe Map Keys**: An immutable object's `hashCode()` never changes, making it ideal for `HashMap` and `HashSet`.
* **Zero Side-Effects**: Methods cannot unintentionally alter caller state.

### Disadvantages
* **Object Allocation Overhead**: Creating a new object for every single tiny state change generates garbage on the heap (though modern JVM HotSpot garbage collectors handle short-lived objects very efficiently).
* **Boilerplate for Deep Trees**: Updating a deeply nested property in an immutable tree requires reconstructing the ancestors (e.g., `user.withAddress(user.getAddress().withCity("New City"))`).

---

## 8. When to Use It and When Not to Use It

### Default to Immutability For:
* **Value Objects** (e.g., `Money`, `Coordinates`, `DateRange`, `Address`).
* Configuration objects, event payloads, and Data Transfer Objects (DTOs).
* Shared cache entries and `HashMap` keys.
* Concurrent and multithreaded systems.

### Tolerate Mutability For:
* High-frequency state mutation algorithms (e.g., rendering game loops at 120 FPS, array sorting).
* Stateful entities with extensive lifecycles where creating millions of copies would cause GC pauses (e.g., an active `ShoppingCart` session).

---

## 9. Common Mistakes and Misconceptions

* **Mistake: Assuming `final` makes an object immutable**:
  `final List<Item> items = new ArrayList<>();` only prevents reassigning the variable `items`. Any caller can still execute `items.clear()` or `items.add(...)`! You must use `List.copyOf()` or `Collections.unmodifiableList()`.
* **Mistake: Forgetting defensive copying on mutable constructor inputs**:
  If a constructor accepts a mutable `Date` or `List` and assigns it directly, the caller still holds the original reference and can mutate it from the outside.

---

## 10. Common LLD Interview Questions with Answers

### Q1: Why is `java.lang.String` designed to be immutable in Java?
**Answer**:
1. **Security**: Strings carry sensitive data (passwords, network URLs, database connection strings, file paths); immutability prevents malicious tampering.
2. **String Pool (Flyweight Pattern)**: The JVM can safely share identical string literals across the entire application to save memory.
3. **Thread Safety**: Strings can be shared across multiple threads without locking.
4. **HashCode Caching**: The hash code is calculated once and cached, making hash table lookups fast and reliable.

### Q2: What is the difference between Shallow Immutability and Deep Immutability?
**Answer**:
* **Shallow Immutability**: All fields of the top-level object are `final`, but some fields reference mutable objects (e.g., a `final List`). The container reference cannot change, but the referenced object's internal state can still be modified.
* **Deep Immutability**: The entire object graph is immutable; all referenced nested objects and collections are also completely immutable and cannot be altered.

### Q3: When should you use a Java `record` over a traditional immutable class?
**Answer**: Use a `record` whenever the entity is a transparent data carrier whose identity is defined solely by its fields. Records automatically provide private final fields, a canonical constructor, compact constructors for validation, and proper implementations of `equals()`, `hashCode()`, and `toString()`. Use a traditional class when you need custom inheritance hierarchies or private hidden representations.

---

## 11. Short Revision Notes

* **Core Concept**: State is permanently frozen upon object construction.
* **Golden Rules**: `final` class, `private final` fields, no setters, defensive copying.
* **Concurrency Superpower**: 100% thread-safe with **zero synchronization locks**.
* **Modern Java Tool**: Use `record` for clean, boilerplate-free immutable data objects.
