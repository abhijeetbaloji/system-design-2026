package com.systemdesign.lld.patterns.composite.example02.good;

import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating client-side polymorphic operations on the organizational hierarchy.
 */
public class PayrollService {

    public double calculateTotalPayroll(OrganizationComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        return component.getSalary();
    }

    public double calculateAverageSalary(OrganizationComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        int headcount = component.getHeadcount();
        if (headcount == 0) {
            return 0.0;
        }
        return component.getSalary() / headcount;
    }

    public String generateDepartmentSummary(OrganizationComponent component) {
        Objects.requireNonNull(component, "Component cannot be null");
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(" CORPORATE PAYROLL & STRUCTURE: ").append(component.getName()).append("\n");
        sb.append("====================================================\n");
        sb.append(component.printHierarchy(""));
        sb.append("====================================================\n");
        sb.append("Total Headcount: ").append(component.getHeadcount()).append("\n");
        sb.append("Total Payroll:   $").append(String.format("%,.2f", component.getSalary())).append("\n");
        sb.append("Average Salary:  $").append(String.format("%,.2f", calculateAverageSalary(component))).append("\n");
        return sb.toString();
    }

    public List<OrganizationComponent> findPersonnelByRole(OrganizationComponent component, String role) {
        Objects.requireNonNull(component, "Component cannot be null");
        Objects.requireNonNull(role, "Role cannot be null");
        return component.findByRole(role);
    }
}
