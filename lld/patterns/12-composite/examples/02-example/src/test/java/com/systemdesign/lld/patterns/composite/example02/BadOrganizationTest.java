package com.systemdesign.lld.patterns.composite.example02;

import com.systemdesign.lld.patterns.composite.example02.bad.BadDepartment;
import com.systemdesign.lld.patterns.composite.example02.bad.BadEmployee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bad Design: Procedural Organization Hierarchy Tests")
class BadOrganizationTest {

    @Test
    @DisplayName("BadDepartment requires dual lists and procedural recursion")
    void testBadDepartmentAggregations() {
        BadDepartment root = new BadDepartment("Corporate");
        root.addEmployee(new BadEmployee("Boss", "CEO", 200_000));

        BadDepartment dev = new BadDepartment("Dev");
        dev.addEmployee(new BadEmployee("Dev1", "SWE", 100_000));
        root.addSubDepartment(dev);

        assertEquals(300_000, root.calculateTotalSalary());
        assertEquals(2, root.calculateHeadcount());
        assertEquals(1, root.getDirectEmployees().size());
        assertEquals(1, root.getSubDepartments().size());
    }
}
