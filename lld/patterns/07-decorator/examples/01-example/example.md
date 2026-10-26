# Example 01: Coffee and Add-ons

## 1. The Problem

A coffee shop ordering system must compute the total price and assemble a descriptive receipt for customized beverages. Customers can order any base beverage (e.g., Espresso, Dark Roast, Decaf) and enrich it with an arbitrary combination of condiments (e.g., Milk, Soy, Mocha, Whipped Cream, Caramel). Furthermore, customers frequently order multiple portions of the same condiment (e.g., double mocha or extra whip).

If modeled using traditional inheritance or flag-based configuration, the system collapses under combinatorial complexity.

---

## 2. Bad Design Analysis

### Flaw 1: Boolean Flags and Monolithic Base Class
In `bad/Beverage.java`, the base class maintains boolean flags (`milk`, `soy`, `mocha`, `whip`) and conditional checks inside `cost()` and `getDescription()`:

```java
public class Beverage {
    private boolean milk;
    private boolean mocha;
    private int mochaCount = 0; // Clunky patch to support multiple shots
    // ...
    public double cost() {
        double condimentCost = 0.0;
        if (milk) condimentCost += 0.50;
        if (mocha) condimentCost += mochaCount * 0.75;
        // ...
        return condimentCost;
    }
}
```

#### Why This Fails:
1. **Violation of the Open/Closed Principle (OCP):** Introducing a new condiment (e.g., `OatMilk`) or changing a condiment price forces modifications to `Beverage.java`, risking regressions across all drinks.
2. **Violation of the Single Responsibility Principle (SRP):** `Beverage` manages both beverage identity and the entire catalog of condiment pricing and combinations.
3. **Inappropriate Inheritance:** Non-coffee beverages (e.g., Green Tea) inherit milk and mocha fields that make no sense in their domain.
4. **Poor Extensibility:** Supporting multiple shots of any condiment requires adding individual counter variables for every condiment in the catalog.

### Flaw 2: Combinatorial Subclass Explosion
In `bad/EspressoWithMilkAndMocha.java`, an attempt is made to bypass base class mutation by subclassing every combination:
- `EspressoWithMilk`
- `EspressoWithMilkAndMocha`
- `DarkRoastWithSoyAndCaramel`

For $B$ base beverages and $C$ condiments, the total number of classes required is $B \times 2^C$. With 5 drinks and 6 condiments, this would require $5 \times 64 = 320$ distinct classes.

---

## 3. Improved Design (Decorator Pattern)

The improved design in `good/` models condiments as **pluggable wrapper layers** around a core beverage:

```
new Caramel(new Soy(new Decaf()))
```

### Participants
- **Component Abstraction (`Beverage`):** Common interface declaring `String getDescription()` and `double getCost()`.
- **Concrete Components (`Espresso`, `DarkRoast`, `Decaf`):** Baseline drinks implementing `Beverage` without any knowledge of condiments.
- **Abstract Decorator (`CondimentDecorator`):** Implements `Beverage` and aggregates a `protected final Beverage beverage` reference.
- **Concrete Decorators (`Milk`, `Mocha`, `Soy`, `Whip`, `Caramel`):** Extend `CondimentDecorator`, delegating to the wrapped `beverage` and augmenting the cost and description.

---

## 4. Execution Flow: How Wrapping and Delegation Work

When the client requests `getCost()` or `getDescription()` on `new Whip(new Mocha(new Mocha(new DarkRoast())))`:

```
Client Invocation: whip.getCost()
 │
 ├──> Whip.getCost()
 │     │
 │     └──> calls mocha2.getCost() + $0.40
 │           │
 │           └──> calls mocha1.getCost() + $0.75
 │                 │
 │                 └──> calls darkRoast.getCost() + $0.75
 │                       │
 │                       └──> returns base $2.49
 │                 │
 │                 └──> returns $2.49 + $0.75 = $3.24
 │           │
 │           └──> returns $3.24 + $0.75 = $3.99
 │     │
 │     └──> returns $3.99 + $0.40 = $4.39
 ▼
Final Price: $4.39
```

---

## 5. Relationship to SOLID Principles

- **Open/Closed Principle (OCP):** Adding a new condiment (e.g., `Hazelnut`) requires writing a single new class `Hazelnut extends CondimentDecorator`. Zero lines of existing code are touched.
- **Single Responsibility Principle (SRP):** `Mocha` only handles chocolate syrup calculations. `DarkRoast` only handles dark roast baseline logic.
- **Liskov Substitution Principle (LSP):** A decorated beverage (`Mocha(Espresso)`) is a `Beverage`. Any receipt printer or billing module expecting a `Beverage` handles it transparently.
- **Dependency Inversion Principle (DIP):** Decorators depend on the abstract `Beverage` interface, never on concrete coffee classes.

---

## 6. Architectural Trade-offs

| Advantage | Cost / Trade-off |
| :--- | :--- |
| Zero subclass explosion; linear class count ($B + C$). | Proliferation of small wrapper objects on the heap. |
| Supports duplicate condiments (double mocha) out of the box. | Instantiation code can become verbose (`new Whip(new Mocha(...))`). |
| Dynamic runtime order modification. | Client code cannot easily inspect the unwrapped inner beverage type. |

---

## 7. How to Identify Incorrect Implementations

Watch for these warning signs in code reviews:
1. **Condiment not implementing `Beverage`:** If `Mocha` is just a utility class that takes `Beverage` in a method parameter without implementing `Beverage`, it is a Helper, not a Decorator.
2. **Hardcoded Wrappee Types:** If `Milk` requires an `Espresso` specifically rather than any `Beverage`, it breaks composability with `DarkRoast` or other decorators.
3. **Mutating the Inner Component:** If the decorator modifies internal mutable state of the wrappee instead of calculating values via delegation, it is violating functional immutability.
