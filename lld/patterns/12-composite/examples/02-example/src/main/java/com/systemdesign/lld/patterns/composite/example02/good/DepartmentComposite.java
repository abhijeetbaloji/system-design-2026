package com.systemdesign.lld.patterns.composite.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Composite component representing a department or division containing employees and sub-departments.
 */
public class DepartmentComposite implements OrganizationComponent {
    private final String name;
    private final List<OrganizationComponent> members = new ArrayList<>();

    public DepartmentComposite(String name) {
        this.name = Objects.requireNonNull(name, "Department name cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    public DepartmentComposite addMember(OrganizationComponent member) {
        Objects.requireNonNull(member, "Member cannot be null");
        members.add(member);
        return this;
    }

    public boolean removeMember(OrganizationComponent member) {
        return members.remove(member);
    }

    public List<OrganizationComponent> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public double getSalary() {
        double totalSalary = 0.0;
        for (OrganizationComponent member : members) {
            totalSalary += member.getSalary();
        }
        return totalSalary;
    }

    @Override
    public int getHeadcount() {
        int totalHeadcount = 0;
        for (OrganizationComponent member : members) {
            totalHeadcount += member.getHeadcount();
        }
        return totalHeadcount;
    }

    @Override
    public String printHierarchy(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("🏢 [Dept] ").append(name)
          .append(" (Headcount: ").append(getHeadcount())
          .append(", Budget: $").append(String.format("%,.2f", getSalary()))
          .append(")\n");
        for (OrganizationComponent member : members) {
            sb.append(member.printHierarchy(indent + "│   "));
        }
        return sb.toString();
    }

    @Override
    public List<OrganizationComponent> findByRole(String role) {
        List<OrganizationComponent> matches = new ArrayList<>();
        for (OrganizationComponent member : members) {
            matches.addAll(member.findByRole(role));
        }
        return matches;
    }

    @Override
    public boolean isDepartment() {
        return true;
    }

    @Override
    public String toString() {
        return "DepartmentComposite{name='" + name + "', membersCount=" + members.size() + "}";
    }
}
