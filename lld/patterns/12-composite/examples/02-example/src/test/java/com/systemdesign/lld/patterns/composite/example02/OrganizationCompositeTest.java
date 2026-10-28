package com.systemdesign.lld.patterns.composite.example02;

import com.systemdesign.lld.patterns.composite.example02.good.DepartmentComposite;
import com.systemdesign.lld.patterns.composite.example02.good.EmployeeLeaf;
import com.systemdesign.lld.patterns.composite.example02.good.OrganizationComponent;
import com.systemdesign.lld.patterns.composite.example02.good.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Composite Pattern Organization Hierarchy Tests")
class OrganizationCompositeTest {

    private DepartmentComposite enterprise;
    private DepartmentComposite engineering;
    private DepartmentComposite qualityAssurance;
    private EmployeeLeaf ceo;
    private EmployeeLeaf leadDev;
    private EmployeeLeaf juniorDev;
    private EmployeeLeaf qaEngineer;
    private PayrollService payrollService;

    @BeforeEach
    void setUp() {
        payrollService = new PayrollService();

        enterprise = new DepartmentComposite("TechCorp");
        engineering = new DepartmentComposite("Engineering");
        qualityAssurance = new DepartmentComposite("QA");

        ceo = new EmployeeLeaf("Alice CEO", "CEO", 300_000);
        leadDev = new EmployeeLeaf("Bob Lead", "Tech Lead", 150_000);
        juniorDev = new EmployeeLeaf("Charlie Jr", "Software Engineer", 90_000);
        qaEngineer = new EmployeeLeaf("Diana QA", "QA Engineer", 80_000);

        engineering.addMember(leadDev);
        engineering.addMember(juniorDev);

        qualityAssurance.addMember(qaEngineer);

        enterprise.addMember(ceo);
        enterprise.addMember(engineering);
        enterprise.addMember(qualityAssurance);
    }

    @Test
    @DisplayName("Employee leaf returns its own salary and headcount of 1")
    void testEmployeeLeaf() {
        assertEquals(300_000, ceo.getSalary());
        assertEquals(1, ceo.getHeadcount());
        assertEquals("Alice CEO", ceo.getName());
        assertEquals("CEO", ceo.getRole());
        assertFalse(ceo.isDepartment());
    }

    @Test
    @DisplayName("Department composite sums salaries and headcount across sub-branches recursively")
    void testDepartmentAggregations() {
        // Engineering: 150,000 + 90,000 = 240,000; Headcount: 2
        assertEquals(240_000, engineering.getSalary());
        assertEquals(2, engineering.getHeadcount());

        // QA: 80,000; Headcount: 1
        assertEquals(80_000, qualityAssurance.getSalary());
        assertEquals(1, qualityAssurance.getHeadcount());

        // Enterprise: 300,000 (CEO) + 240,000 (Eng) + 80,000 (QA) = 620,000; Headcount: 4
        assertEquals(620_000, enterprise.getSalary());
        assertEquals(4, enterprise.getHeadcount());
        assertTrue(enterprise.isDepartment());
    }

    @Test
    @DisplayName("PayrollService calculates accurate average salary across subtrees")
    void testAverageSalaryCalculation() {
        // Engineering: 240,000 / 2 = 120,000
        assertEquals(120_000, payrollService.calculateAverageSalary(engineering), 0.001);

        // Enterprise: 620,000 / 4 = 155,000
        assertEquals(155_000, payrollService.calculateAverageSalary(enterprise), 0.001);

        // Empty department has average salary 0.0
        DepartmentComposite emptyDept = new DepartmentComposite("Empty");
        assertEquals(0.0, payrollService.calculateAverageSalary(emptyDept), 0.001);
    }

    @Test
    @DisplayName("Dynamic member removal updates headcount and budget immediately")
    void testDynamicMemberRemoval() {
        assertEquals(4, enterprise.getHeadcount());
        assertEquals(620_000, enterprise.getSalary());

        boolean removed = enterprise.removeMember(qualityAssurance);
        assertTrue(removed);

        assertEquals(3, enterprise.getHeadcount());
        assertEquals(540_000, enterprise.getSalary());
    }

    @Test
    @DisplayName("Search by role finds matching personnel across all nested departments")
    void testRoleSearch() {
        List<OrganizationComponent> qaMembers = payrollService.findPersonnelByRole(enterprise, "QA Engineer");
        assertEquals(1, qaMembers.size());
        assertEquals("Diana QA", qaMembers.get(0).getName());

        List<OrganizationComponent> leads = payrollService.findPersonnelByRole(enterprise, "Tech Lead");
        assertEquals(1, leads.size());
        assertEquals("Bob Lead", leads.get(0).getName());
    }

    @Test
    @DisplayName("Negative salary throws IllegalArgumentException")
    void testNegativeSalary() {
        assertThrows(IllegalArgumentException.class, () -> new EmployeeLeaf("Invalid", "Dev", -500));
    }
}
