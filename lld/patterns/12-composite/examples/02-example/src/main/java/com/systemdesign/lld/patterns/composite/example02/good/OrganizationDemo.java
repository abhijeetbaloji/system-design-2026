package com.systemdesign.lld.patterns.composite.example02.good;

import java.util.List;

public class OrganizationDemo {
    public static void main(String[] args) {
        // Build company tree
        DepartmentComposite enterprise = new DepartmentComposite("TechCorp Global");

        // Executive team
        enterprise.addMember(new EmployeeLeaf("Ada Lovelace", "Chief Executive Officer", 450000));
        enterprise.addMember(new EmployeeLeaf("Alan Turing", "Chief Technology Officer", 380000));

        // Engineering division
        DepartmentComposite engineering = new DepartmentComposite("Engineering Division");
        engineering.addMember(new EmployeeLeaf("Barbara Liskov", "VP of Engineering", 280000));

        // Platform sub-department
        DepartmentComposite platform = new DepartmentComposite("Platform Engineering");
        platform.addMember(new EmployeeLeaf("Dennis Ritchie", "Staff Engineer", 195000));
        platform.addMember(new EmployeeLeaf("Ken Thompson", "Senior Engineer", 160000));

        // Frontend sub-department
        DepartmentComposite frontend = new DepartmentComposite("Frontend Experience");
        frontend.addMember(new EmployeeLeaf("Brendan Eich", "Principal Engineer", 210000));
        frontend.addMember(new EmployeeLeaf("Alice", "Frontend Engineer", 130000));

        engineering.addMember(platform);
        engineering.addMember(frontend);
        enterprise.addMember(engineering);

        PayrollService service = new PayrollService();

        // Print entire organization summary
        System.out.println(service.generateDepartmentSummary(enterprise));

        // Print sub-department summary polymorphically
        System.out.println(service.generateDepartmentSummary(platform));

        // Search for all Staff / Principal engineers
        List<OrganizationComponent> staffEngineers = service.findPersonnelByRole(enterprise, "Staff Engineer");
        System.out.println("Found " + staffEngineers.size() + " Staff Engineer(s):");
        for (OrganizationComponent member : staffEngineers) {
            System.out.println(" - " + member.getName());
        }
    }
}
