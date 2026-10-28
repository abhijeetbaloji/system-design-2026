package com.systemdesign.lld.patterns.composite.example02.bad;

public class BadOrganizationDemo {
    public static void main(String[] args) {
        BadDepartment company = new BadDepartment("Acme Corp");

        BadDepartment engineering = new BadDepartment("Engineering");
        engineering.addEmployee(new BadEmployee("Grace Hopper", "VP of Engineering", 220000));

        BadDepartment backend = new BadDepartment("Backend Services");
        backend.addEmployee(new BadEmployee("Linus", "Principal Engineer", 180000));
        backend.addEmployee(new BadEmployee("Ken", "Senior Software Engineer", 140000));

        engineering.addSubDepartment(backend);
        company.addSubDepartment(engineering);

        System.out.println("=== Bad Org Hierarchy ===");
        System.out.print(company.displayHierarchy(""));

        System.out.println("Total Company Salary: $" + company.calculateTotalSalary());
        System.out.println("Total Headcount: " + company.calculateHeadcount());
    }
}
