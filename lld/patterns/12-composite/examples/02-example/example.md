# Organization Hierarchy Composite Example

## 1. Scenario Overview

In modern enterprises, organizational structure is inherently hierarchical:
- An enterprise contains multiple divisions (e.g., "Engineering", "Sales", "Human Resources").
- A division contains specialized sub-departments (e.g., "Cloud Infrastructure", "Frontend Engineering", "Security").
- A department contains individual contributors (Engineers, Designers, Product Managers) as well as managers who lead teams.

Financial and HR systems must compute metrics across any level of this hierarchy:
- **Total Payroll / Budget**: Aggregated salary costs for an entire division, a specific team, or a single employee.
- **Headcount**: The total number of human workers under any node.
- **Org Chart Traversal & Role Search**: Finding all employees matching a specific role (e.g., "Staff Engineer") across nested departments.

Without the Composite pattern, systems treat departments and employees as completely separate entities, requiring nested dual lists, complex type checking, and fragile manual recursion. With the Composite pattern, both `EmployeeLeaf` and `DepartmentComposite` implement `OrganizationComponent`, letting payroll services and HR dashboards query any subtree polymorphically.

---

## 2. Bad Design vs Good Design

### Bad Design (Separated Structures with Dual Iteration)
- `BadDepartment` has `List<BadEmployee>` and `List<BadDepartment>`.
- Calculating payroll requires two separate loops: one for direct employees and another for recursive calls on sub-departments.
- Calculating headcount is duplicated similarly.
- Cannot pass an individual employee or a department interchangeably to payroll calculators.

### Good Design (Composite Pattern)
- `OrganizationComponent` provides a clean, unified abstraction: `getName()`, `getSalary()`, `getHeadcount()`, `printHierarchy()`, and `findByRole()`.
- `EmployeeLeaf` represents an individual employee, returning their salary directly and a headcount of 1.
- `DepartmentComposite` manages a collection of `OrganizationComponent` members (which can be individual employees or nested sub-departments) and aggregates metrics recursively.
- `PayrollService` computes total compensation and averages without knowing whether the input is an individual person, a five-person squad, or a multinational 5,000-person division.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.composite.example02
├── bad
│   ├── BadDepartment.java
│   ├── BadEmployee.java
│   └── BadOrganizationDemo.java
└── good
    ├── DepartmentComposite.java
    ├── EmployeeLeaf.java
    ├── OrganizationComponent.java
    ├── OrganizationDemo.java
    └── PayrollService.java
```

---

## 4. Execution Flow & SOLID Principles

- **Recursive Metric Rollup**: When `getSalary()` is invoked on a top-level `DepartmentComposite`, it iterates over all children. Leaves return their individual base salary; child departments invoke their own children recursively and return the aggregated sub-total.
- **Single Responsibility Principle (SRP)**: `EmployeeLeaf` holds employee compensation details; `DepartmentComposite` coordinates structural relationships; `PayrollService` handles high-level analytics and reporting.
- **Open/Closed Principle (OCP)**: Introducing contractor leaves (e.g., `ContractorLeaf` with hourly billing) or cross-functional matrix pods requires zero changes to `DepartmentComposite` or `PayrollService`.
