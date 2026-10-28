package com.systemdesign.lld.patterns.composite.example02.bad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadDepartment {
    private final String name;
    private final List<BadEmployee> directEmployees = new ArrayList<>();
    private final List<BadDepartment> subDepartments = new ArrayList<>();

    public BadDepartment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addEmployee(BadEmployee employee) {
        directEmployees.add(employee);
    }

    public void addSubDepartment(BadDepartment department) {
        subDepartments.add(department);
    }

    public List<BadEmployee> getDirectEmployees() {
        return Collections.unmodifiableList(directEmployees);
    }

    public List<BadDepartment> getSubDepartments() {
        return Collections.unmodifiableList(subDepartments);
    }

    // Flawed: Must maintain two loops for salary calculation
    public double calculateTotalSalary() {
        double total = 0.0;
        for (BadEmployee emp : directEmployees) {
            total += emp.getSalary();
        }
        for (BadDepartment dept : subDepartments) {
            total += dept.calculateTotalSalary();
        }
        return total;
    }

    // Flawed: Must maintain two loops for headcount
    public int calculateHeadcount() {
        int count = directEmployees.size();
        for (BadDepartment dept : subDepartments) {
            count += dept.calculateHeadcount();
        }
        return count;
    }

    // Flawed: Manual indentation and hierarchy printing
    public String displayHierarchy(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("[Dept] ").append(name).append("\n");
        for (BadEmployee emp : directEmployees) {
            sb.append(emp.print(indent + "  "));
        }
        for (BadDepartment dept : subDepartments) {
            sb.append(dept.displayHierarchy(indent + "  "));
        }
        return sb.toString();
    }
}
