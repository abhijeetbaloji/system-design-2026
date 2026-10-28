# Example 01: Document Templates (Invoices and Deep Copying)

## 1. Problem Scenario
An enterprise billing and document generation platform creates thousands of monthly invoices. Constructing an invoice from scratch requires:
- Initializing corporate branding, terms of service, tax calculations, and baseline metadata.
- Pre-populating recurring baseline line items (e.g., standard platform hosting fee, base maintenance retainer).

Without the Prototype pattern:
- Developers manually instantiate `BadInvoice` and attempt to copy lists using `target.setItems(source.getItems())`.
- This performs a **shallow copy**: both invoices now share the exact same `ArrayList` and `LineItem` instances in memory.
- When customer B's invoice adds custom consulting hours or modifies an item price, customer A's invoice is silently mutated, causing catastrophic billing errors and data corruption.

## 2. Bad Design (Leaky Shallow Copy)
- In the bad design, clients copy objects by extracting getter values and assigning shared list references.
- `badInvoice2.getItems().add(new BadLineItem(...))` directly modifies the list inside `badInvoice1`.
- The baseline template is mutated and destroyed in memory.

## 3. Good Design (Prototype Pattern)
- `Prototype<T>`: Generic type-safe cloning contract.
- `Document`: Abstract base class implementing `Prototype<Document>`, featuring a protected copy constructor that deeply duplicates the metadata map.
- `LineItem`: Deep-copyable value component implementing `Prototype<LineItem>`.
- `InvoiceDocument`: Refined prototype providing deep copying:
  - Iterates through the source invoice's `items` list and calls `.clone()` on every single `LineItem`.
  - Creates a brand new, isolated `ArrayList` in the clone.
- `DocumentPrototypeRegistry`: Pre-caches golden baseline templates (e.g., `"standard-saas-invoice"`).
  - Calling `registry.get("standard-saas-invoice")` always returns a freshly cloned, completely independent copy.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/19-prototype/examples/01-example/diagram.mmd).

## 5. Execution Flow
1. Baseline invoice template is constructed with standard metadata and retainer line items.
2. Template is registered in `DocumentPrototypeRegistry` under `"monthly-retainer"`.
3. Client requests an invoice for Customer A: `registry.get("monthly-retainer")`.
4. Client adds hours and updates invoice number for Customer A.
5. Client requests an invoice for Customer B: `registry.get("monthly-retainer")`.
6. Client verifies Customer B's invoice starts with clean baseline items, completely unaffected by Customer A's mutations.
