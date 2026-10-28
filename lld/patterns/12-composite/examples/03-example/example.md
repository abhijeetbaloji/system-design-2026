# Restaurant Menu Categories Composite Example

## 1. Scenario Overview

Restaurant menus naturally form part-whole tree hierarchies:
- A master restaurant menu contains broad categories like **Appetizers**, **Main Course**, **Desserts**, and **Beverages**.
- A category like **Beverages** can contain standalone drink items (e.g., Espresso, Sparkling Water) as well as sub-categories like **Craft Beers** or **Signature Cocktails**.
- Individual items (Leaf nodes) have a name, description, price, and vegetarian flag.
- Categories (Composite nodes) aggregate items and nested sub-categories, providing a unified view of menu sections.

Client applications (online ordering apps, waitstaff POS terminals, dynamic digital signage) need to:
1. Render formatted menus with nested section headers and indented items (`print()`).
2. Calculate total pricing or average category cost (`getPrice()`).
3. Filter menus for dietary restrictions (e.g., extracting all vegetarian options via `filterVegetarian()`).

Without the Composite pattern, menu management systems maintain separate lists for dishes and sub-categories, resulting in fragile dual-loop traversals. With the Composite pattern, both `MenuItemLeaf` and `MenuCategoryComposite` implement `MenuComponent`, enabling uniform recursive operations across arbitrary menu depth.

---

## 2. Bad Design vs Good Design

### Bad Design (Dual Lists & Procedural Branching)
- `BadMenuCategory` maintains `List<BadMenuItem>` and `List<BadMenuCategory>`.
- Operations like calculating total value, printing the menu, or searching for vegetarian dishes require manual dual loops at every nesting level.
- Cannot treat a standalone combo dish and a sub-menu section uniformly in ordering carts or promotional engines.

### Good Design (Composite Pattern)
- `MenuComponent` establishes the uniform contract (`getName()`, `getDescription()`, `getPrice()`, `isVegetarian()`, `print()`, `filterVegetarian()`).
- `MenuItemLeaf` encapsulates single dish properties (e.g., "Tiramisu", $8.50, vegetarian).
- `MenuCategoryComposite` holds a `List<MenuComponent>`, delegating operations recursively to dishes and sub-categories alike.
- `MenuCatalogService` renders menus, calculates totals, and filters dietary options polymorphically.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.composite.example03
├── bad
│   ├── BadMenuCategory.java
│   ├── BadMenuItem.java
│   └── BadRestaurantMenuDemo.java
└── good
    ├── MenuCatalogService.java
    ├── MenuCategoryComposite.java
    ├── MenuComponent.java
    ├── MenuItemLeaf.java
    └── RestaurantMenuDemo.java
```

---

## 4. Execution Flow & SOLID Principles

- **Polymorphic Printing**: Calling `print("")` on the root menu triggers a preorder depth-first traversal, printing category headers, followed by indented dishes and nested sub-category trees.
- **Dietary Filtering**: Calling `filterVegetarian()` recursively collects all vegetarian items from both standalone items and nested sub-menus into a flat or structured list.
- **Open/Closed Principle (OCP)**: Adding new menu types like `ComboMealComposite` (bundle of items with fixed package pricing) requires no changes to `MenuCategoryComposite` or `MenuCatalogService`.
- **Single Responsibility Principle (SRP)**: `MenuItemLeaf` models culinary properties; `MenuCategoryComposite` models organizational grouping; `MenuCatalogService` handles business presentations.
