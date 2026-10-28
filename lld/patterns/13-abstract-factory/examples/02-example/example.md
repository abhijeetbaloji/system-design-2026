# Compatible Furniture Families Abstract Factory Example

## 1. Scenario Overview

An interior design company manufactures furniture suites across distinct architectural and aesthetic styles:
1. **Modern**: Minimalist steel and tempered glass designs, sleek geometric lines, neutral monochrome colors.
2. **Victorian**: Carved mahogany wood, velvet upholstery, ornate gilded floral motifs, traditional four-legged designs.
3. **Art Deco**: Bold zigzag geometric shapes, polished brass accents, opulent 1920s glamour.

Each aesthetic family provides three core living room products:
- `Chair` (`sitOn()`, `hasLegs()`, `getStyle()`)
- `Sofa` (`layOn()`, `getCapacity()`, `getStyle()`)
- `CoffeeTable` (`placeItem()`, `getStyle()`)

When a homeowner or interior architect orders a living room package (`LivingRoomSuite`), all three items must belong to the exact same design language. Mixing a Victorian carved wooden sofa with a Modern glass coffee table results in an aesthetic clash.

---

## 2. Bad Design vs Good Design

### Bad Design (Manual Assembly / Scattered Branching)
- `BadFurnitureStore` uses string/enum parameters and `switch` statements to pick individual items.
- A bug or mistaken parameter can pair a `VictorianSofa` with a `ModernCoffeeTable`.
- Adding a new style (like `Industrial` or `Scandinavian`) requires modifying every switch statement in the catalog service.

### Good Design (Abstract Factory Pattern)
- `FurnitureFactory` declares `createChair()`, `createSofa()`, and `createCoffeeTable()`.
- `ModernFurnitureFactory`, `VictorianFurnitureFactory`, and `ArtDecoFurnitureFactory` guarantee that all created pieces match each other in style.
- `LivingRoomSuite` receives a `FurnitureFactory` and assembles a visually harmonious living room suite without knowing concrete implementation classes.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.abstractfactory.example02
├── bad
│   ├── BadFurnitureDemo.java
│   ├── BadFurnitureStore.java
│   ├── ModernChair.java
│   ├── ModernCoffeeTable.java
│   ├── ModernSofa.java
│   ├── VictorianChair.java
│   ├── VictorianCoffeeTable.java
│   └── VictorianSofa.java
└── good
    ├── ArtDecoChair.java
    ├── ArtDecoCoffeeTable.java
    ├── ArtDecoFurnitureFactory.java
    ├── ArtDecoSofa.java
    ├── Chair.java
    ├── CoffeeTable.java
    ├── FurnitureFactory.java
    ├── FurnitureStoreDemo.java
    ├── LivingRoomSuite.java
    ├── ModernChair.java
    ├── ModernCoffeeTable.java
    ├── ModernFurnitureFactory.java
    ├── ModernSofa.java
    ├── Sofa.java
    ├── VictorianChair.java
    ├── VictorianCoffeeTable.java
    ├── VictorianFurnitureFactory.java
    └── VictorianSofa.java
```

---

## 4. Execution Flow & SOLID Principles

- **Style Guarantee**: The client cannot request a Modern Chair and a Victorian Sofa together because the suite is constructed from a single factory instance.
- **Open/Closed Principle (OCP)**: Introducing a `ScandinavianFurnitureFactory` requires zero modifications to existing factories or `LivingRoomSuite`.
- **Single Responsibility Principle (SRP)**: Each factory is responsible purely for manufacturing items within its aesthetic family.
