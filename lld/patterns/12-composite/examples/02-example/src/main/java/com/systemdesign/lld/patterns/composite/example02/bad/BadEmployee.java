package com.systemdesign.lld.patterns.composite.example02.bad;

public class BadEmployee {
    private final String name;
    private final String role;
    private final double salary;

    public BadEmployee(String name, String role, double salary) {
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public String print(String indent) {
        return indent + "- " + name + " (" + role + ", $" + String.format("%.2f", salary) + ")\n";
    }
}
