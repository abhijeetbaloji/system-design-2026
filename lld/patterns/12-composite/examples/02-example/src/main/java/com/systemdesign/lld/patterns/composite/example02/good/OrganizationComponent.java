package com.systemdesign.lld.patterns.composite.example02.good;

import java.util.List;

/**
 * Component interface representing both individual employees and organizational departments.
 */
public interface OrganizationComponent {
    String getName();

    double getSalary();

    int getHeadcount();

    String printHierarchy(String indent);

    List<OrganizationComponent> findByRole(String role);

    default boolean isDepartment() {
        return false;
    }
}
