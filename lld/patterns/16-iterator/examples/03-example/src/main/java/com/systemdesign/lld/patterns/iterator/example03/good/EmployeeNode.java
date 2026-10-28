package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain entity representing an employee in the organizational tree.
 * Encapsulates internal hierarchy and exposes safe unmodifiable views.
 */
public class EmployeeNode {
    private final String id;
    private final String name;
    private final String title;
    private final String department;
    private final double salary;
    private final List<EmployeeNode> directReports = new ArrayList<>();

    public EmployeeNode(String id, String name, String title, String department, double salary) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.salary = salary;
    }

    public void addDirectReport(EmployeeNode subordinate) {
        directReports.add(Objects.requireNonNull(subordinate, "Subordinate cannot be null"));
    }

    public List<EmployeeNode> getDirectReports() {
        return Collections.unmodifiableList(directReports);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String formatSummary() {
        return String.format("[%s] %s - %s (%s, $%,.0f)", id, name, title, department, salary);
    }

    @Override
    public String toString() {
        return formatSummary();
    }
}
