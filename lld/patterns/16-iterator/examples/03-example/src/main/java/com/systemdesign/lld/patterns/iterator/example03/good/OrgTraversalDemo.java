package com.systemdesign.lld.patterns.iterator.example03.good;

import java.util.Map;

public class OrgTraversalDemo {
    public static void main(String[] args) {
        // Build corporate tree
        EmployeeNode ceo = new EmployeeNode("E-100", "Alice Smith", "Chief Executive Officer", "Executive", 450000);

        EmployeeNode cto = new EmployeeNode("E-200", "Bob Johnson", "Chief Technology Officer", "Engineering", 320000);
        EmployeeNode cfo = new EmployeeNode("E-300", "Carol Williams", "Chief Financial Officer", "Finance", 310000);

        EmployeeNode engDirector = new EmployeeNode("E-210", "Dave Miller", "Director of Engineering", "Engineering", 220000);
        EmployeeNode qaDirector = new EmployeeNode("E-220", "Grace Hopper", "Director of QA", "Quality Assurance", 210000);

        EmployeeNode leadDev = new EmployeeNode("E-211", "Eve Davis", "Principal Architect", "Engineering", 160000);
        EmployeeNode srDev = new EmployeeNode("E-212", "Frank Wright", "Senior Developer", "Engineering", 130000);

        EmployeeNode controller = new EmployeeNode("E-310", "Henry Ford", "Corporate Controller", "Finance", 190000);

        // Assemble hierarchy
        ceo.addDirectReport(cto);
        ceo.addDirectReport(cfo);

        cto.addDirectReport(engDirector);
        cto.addDirectReport(qaDirector);

        cfo.addDirectReport(controller);

        engDirector.addDirectReport(leadDev);
        engDirector.addDirectReport(srDev);

        OrganizationTree tree = new OrganizationTree(ceo);
        OrgReportingService service = new OrgReportingService();

        System.out.println(service.generateChainOfCommandReport(tree));
        System.out.println(service.generateTierLevelReport(tree));

        Map<Integer, Double> tierBudgets = service.calculateTierSalaries(tree);
        System.out.println("=== TIER SALARY BUDGETS ===");
        tierBudgets.forEach((tier, total) ->
                System.out.printf(" Tier %d: $%,.0f%n", tier, total));
    }
}
