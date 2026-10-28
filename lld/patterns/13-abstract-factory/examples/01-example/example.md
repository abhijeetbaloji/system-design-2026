# Cross-Platform UI Abstract Factory Example

## 1. Scenario Overview

A cross-platform desktop application (such as Spotify, Slack, or VS Code) must render native visual components across operating systems:
- **Windows**: Components follow the Fluent Design System (sharp rect borders, Segoe UI font, distinctive toggle animations).
- **macOS**: Components follow the Apple Human Interface Guidelines (rounded pill corners, San Francisco typography, blue accent colors).

Each platform family consists of:
1. `Button` (e.g., `WindowsButton`, `MacButton`)
2. `Checkbox` (e.g., `WindowsCheckbox`, `MacCheckbox`)

The business layer (`Application`) renders a login dialog containing a submit button and a "Remember Me" checkbox. Mixing a Windows button with a Mac checkbox creates an inconsistent visual glitch.

Without the Abstract Factory pattern, client code performs runtime `if-else` checks or hardcodes concrete constructors. With the Abstract Factory pattern, the client depends on `GUIFactory` and product interfaces (`Button`, `Checkbox`), guaranteeing 100% platform family consistency.

---

## 2. Bad Design vs Good Design

### Bad Design (Scattered Conditionals & Inconsistent Instantiation)
- `BadUiApplication` checks `osType` with `if-else` statements.
- Accidental copy-paste errors easily cause cross-contamination (e.g., instantiating `MacCheckbox` inside a Windows app).
- Adding support for `Linux` requires modifying every UI class and branching statement across the entire application.

### Good Design (Abstract Factory Pattern)
- `GUIFactory` defines `createButton()` and `createCheckbox()`.
- `WindowsGUIFactory` produces only Windows-native components; `MacGUIFactory` produces only macOS-native components.
- `Application` receives a `GUIFactory` via dependency injection and instantiates widgets through it.
- Adding a `LinuxGUIFactory` requires zero modifications to existing factories or `Application` client code (adhering strictly to OCP).

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.abstractfactory.example01
├── bad
│   ├── BadUiApplication.java
│   ├── BadUiDemo.java
│   ├── MacButton.java
│   ├── MacCheckbox.java
│   ├── WindowsButton.java
│   └── WindowsCheckbox.java
└── good
    ├── Application.java
    ├── Button.java
    ├── Checkbox.java
    ├── GUIFactory.java
    ├── MacButton.java
    ├── MacCheckbox.java
    ├── MacGUIFactory.java
    ├── UiDemo.java
    ├── WindowsButton.java
    ├── WindowsCheckbox.java
    └── WindowsGUIFactory.java
```

---

## 4. Execution Flow & SOLID Principles

- **Family Consistency**: A single injected factory instance ensures that every component instantiated belongs to the same family.
- **Dependency Inversion Principle (DIP)**: `Application` depends exclusively on `GUIFactory`, `Button`, and `Checkbox` interfaces, never on `WindowsButton` or `MacButton`.
- **Open/Closed Principle (OCP)**: New operating systems or custom visual themes can be added by implementing a new `GUIFactory` and concrete components.
