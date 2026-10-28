package com.systemdesign.lld.patterns.iterator.example03.bad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Flawed design: Employee node mixes entity state with hardcoded traversal methods.
 * Adding new traversal orders requires modifying the entity class.
 * Raw mutable lists are exposed directly to clients.
 */
public class BadEmployee {
    private final String name;
    private final String title;
    private final double salary;
    private final List<BadEmployee> subordinates = new ArrayList<>();

    public BadEmployee(String name, String title, double salary) {
        this.name = name;
        this.title = title;
        this.salary = salary;
    }

    public void addSubordinate(BadEmployee subordinate) {
        subordinates.add(subordinate);
    }

    // Flawed: Exposing raw internal child list
    public List<BadEmployee> getSubordinates() {
        return subordinates;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public double getSalary() {
        return salary;
    }

    // Flawed: Hardcoded DFS recursive printing embedded directly in domain model
    public void printDfs(int depth, StringBuilder output) {
        output.append("  ".repeat(depth))
              .append(title).append(": ").append(name)
              .append(" ($").append((long) salary).append(")\n");
        for (BadEmployee sub : subordinates) {
            sub.printDfs(depth + 1, output);
        }
    }

    // Flawed: Hardcoded BFS printing embedded in domain model
    public void printBfs(StringBuilder output) {
        Queue<BadEmployee> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            BadEmployee current = queue.poll();
            output.append(current.getTitle()).append(": ").append(current.getName()).append("\n");
            queue.addAll(current.getSubordinates());
        }
    }
}
