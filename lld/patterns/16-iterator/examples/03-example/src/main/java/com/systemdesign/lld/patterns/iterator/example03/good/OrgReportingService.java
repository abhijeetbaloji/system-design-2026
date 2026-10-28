package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service demonstrating consumption of the organizational tree via different iterators.
 */
public class OrgReportingService {

    public String generateChainOfCommandReport(OrganizationTree orgTree) {
        Objects.requireNonNull(orgTree, "Organization tree cannot be null");
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(" EXECUTIVE CHAIN OF COMMAND AUDIT (Depth-First Traversal)\n");
        sb.append("=================================================================\n");

        OrgIterator iterator = orgTree.dfsIterator();
        while (iterator.hasNext()) {
            EmployeeNode emp = iterator.next();
            int depth = iterator.getCurrentDepth();
            String indent = "  ".repeat(depth);
            String prefix = depth == 0 ? "[ROOT] " : "|-- ";
            sb.append(indent).append(prefix).append(emp.formatSummary()).append("\n");
        }
        sb.append("=================================================================\n");
        return sb.toString();
    }

    public String generateTierLevelReport(OrganizationTree orgTree) {
        Objects.requireNonNull(orgTree, "Organization tree cannot be null");
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(" CORPORATE TIER LEVEL-ORDER AUDIT (Breadth-First Traversal)\n");
        sb.append("=================================================================\n");

        OrgIterator iterator = orgTree.bfsIterator();
        int lastDepth = -1;
        while (iterator.hasNext()) {
            EmployeeNode emp = iterator.next();
            int depth = iterator.getCurrentDepth();
            if (depth != lastDepth) {
                lastDepth = depth;
                sb.append(String.format("\n--- TIER LEVEL %d ---\n", depth));
            }
            sb.append("  * ").append(emp.formatSummary()).append("\n");
        }
        sb.append("=================================================================\n");
        return sb.toString();
    }

    public Map<Integer, Double> calculateTierSalaries(OrganizationTree orgTree) {
        Objects.requireNonNull(orgTree, "Organization tree cannot be null");
        Map<Integer, Double> tierSalaries = new LinkedHashMap<>();

        OrgIterator iterator = orgTree.bfsIterator();
        while (iterator.hasNext()) {
            EmployeeNode emp = iterator.next();
            int depth = iterator.getCurrentDepth();
            tierSalaries.put(depth, tierSalaries.getOrDefault(depth, 0.0) + emp.getSalary());
        }
        return tierSalaries;
    }
}
