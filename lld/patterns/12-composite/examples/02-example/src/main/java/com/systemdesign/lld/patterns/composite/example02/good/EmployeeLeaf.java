package com.systemdesign.lld.patterns.composite.example02.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Leaf component representing an individual employee in the organization.
 */
public class EmployeeLeaf implements OrganizationComponent {
    private final String name;
    private final String role;
    private final double salary;

    public EmployeeLeaf(String name, String role, double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative: " + salary);
        }
        this.name = Objects.requireNonNull(name, "Employee name cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.salary = salary;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public int getHeadcount() {
        return 1;
    }

    @Override
    public String printHierarchy(String indent) {
        return indent + "├── 👤 " + name + " [" + role + "] - $" + String.format("%,.2f", salary) + "\n";
    }

    @Override
    public List<OrganizationComponent> findByRole(String targetRole) {
        List<OrganizationComponent> matches = new ArrayList<>();
        if (this.role.equalsIgnoreCase(targetRole)) {
            matches.add(this);
        }
        return matches;
    }

    @Override
    public String toString() {
        return "EmployeeLeaf{name='" + name + "', role='" + role + "', salary=" + salary + "}";
    }
}
