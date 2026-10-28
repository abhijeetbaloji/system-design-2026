package com.systemdesign.lld.patterns.iterator.example03;

import com.systemdesign.lld.patterns.iterator.example03.good.BreadthFirstOrgIterator;
import com.systemdesign.lld.patterns.iterator.example03.good.DepthFirstOrgIterator;
import com.systemdesign.lld.patterns.iterator.example03.good.EmployeeNode;
import com.systemdesign.lld.patterns.iterator.example03.good.OrgIterator;
import com.systemdesign.lld.patterns.iterator.example03.good.OrgReportingService;
import com.systemdesign.lld.patterns.iterator.example03.good.OrganizationTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Organization Tree Iterator Pattern Tests")
class OrgIteratorTest {

    private OrganizationTree tree;
    private EmployeeNode ceo;

    @BeforeEach
    void setUp() {
        ceo = new EmployeeNode("E-100", "Alice Smith", "Chief Executive Officer", "Executive", 450000);

        EmployeeNode cto = new EmployeeNode("E-200", "Bob Johnson", "Chief Technology Officer", "Engineering", 320000);
        EmployeeNode cfo = new EmployeeNode("E-300", "Carol Williams", "Chief Financial Officer", "Finance", 310000);

        EmployeeNode engDirector = new EmployeeNode("E-210", "Dave Miller", "Director of Engineering", "Engineering", 220000);
        EmployeeNode qaDirector = new EmployeeNode("E-220", "Grace Hopper", "Director of QA", "Quality Assurance", 210000);

        EmployeeNode leadDev = new EmployeeNode("E-211", "Eve Davis", "Principal Architect", "Engineering", 160000);
        EmployeeNode srDev = new EmployeeNode("E-212", "Frank Wright", "Senior Developer", "Engineering", 130000);

        EmployeeNode controller = new EmployeeNode("E-310", "Henry Ford", "Corporate Controller", "Finance", 190000);

        ceo.addDirectReport(cto);
        ceo.addDirectReport(cfo);

        cto.addDirectReport(engDirector);
        cto.addDirectReport(qaDirector);

        cfo.addDirectReport(controller);

        engDirector.addDirectReport(leadDev);
        engDirector.addDirectReport(srDev);

        tree = new OrganizationTree(ceo);
    }

    @Test
    @DisplayName("DepthFirstOrgIterator traverses in exact preorder DFS sequence and tracks depth")
    void testDepthFirstTraversal() {
        OrgIterator dfs = tree.dfsIterator();
        List<String> visitedIds = new ArrayList<>();
        List<Integer> visitedDepths = new ArrayList<>();

        while (dfs.hasNext()) {
            visitedIds.add(dfs.next().getId());
            visitedDepths.add(dfs.getCurrentDepth());
        }

        // Expected Preorder DFS:
        // CEO(0) -> CTO(1) -> EngDirector(2) -> LeadDev(3) -> SrDev(3) -> QADirector(2) -> CFO(1) -> Controller(2)
        List<String> expectedIds = List.of("E-100", "E-200", "E-210", "E-211", "E-212", "E-220", "E-300", "E-310");
        List<Integer> expectedDepths = List.of(0, 1, 2, 3, 3, 2, 1, 2);

        assertEquals(expectedIds, visitedIds);
        assertEquals(expectedDepths, visitedDepths);

        // Exhaustion throws NoSuchElementException
        assertFalse(dfs.hasNext());
        assertThrows(NoSuchElementException.class, dfs::next);
    }

    @Test
    @DisplayName("BreadthFirstOrgIterator traverses in exact level-order BFS sequence")
    void testBreadthFirstTraversal() {
        OrgIterator bfs = tree.bfsIterator();
        List<String> visitedIds = new ArrayList<>();
        List<Integer> visitedDepths = new ArrayList<>();

        while (bfs.hasNext()) {
            visitedIds.add(bfs.next().getId());
            visitedDepths.add(bfs.getCurrentDepth());
        }

        // Expected BFS Level-Order:
        // Tier 0: CEO(E-100)
        // Tier 1: CTO(E-200), CFO(E-300)
        // Tier 2: EngDirector(E-210), QADirector(E-220), Controller(E-310)
        // Tier 3: LeadDev(E-211), SrDev(E-212)
        List<String> expectedIds = List.of("E-100", "E-200", "E-300", "E-210", "E-220", "E-310", "E-211", "E-212");
        List<Integer> expectedDepths = List.of(0, 1, 1, 2, 2, 2, 3, 3);

        assertEquals(expectedIds, visitedIds);
        assertEquals(expectedDepths, visitedDepths);

        assertFalse(bfs.hasNext());
        assertThrows(NoSuchElementException.class, bfs::next);
    }

    @Test
    @DisplayName("Empty or null root iterator behaves gracefully")
    void testEmptyRootIterator() {
        DepthFirstOrgIterator dfsEmpty = new DepthFirstOrgIterator(null);
        assertFalse(dfsEmpty.hasNext());
        assertThrows(NoSuchElementException.class, dfsEmpty::next);

        BreadthFirstOrgIterator bfsEmpty = new BreadthFirstOrgIterator(null);
        assertFalse(bfsEmpty.hasNext());
        assertThrows(NoSuchElementException.class, bfsEmpty::next);
    }

    @Test
    @DisplayName("OrgReportingService calculates tier budgets accurately using BFS iterator")
    void testTierBudgetCalculation() {
        OrgReportingService service = new OrgReportingService();
        Map<Integer, Double> tierBudgets = service.calculateTierSalaries(tree);

        assertEquals(4, tierBudgets.size());
        assertEquals(450000.0, tierBudgets.get(0)); // CEO
        assertEquals(630000.0, tierBudgets.get(1)); // CTO + CFO (320k + 310k)
        assertEquals(620000.0, tierBudgets.get(2)); // EngDir + QADir + Controller (220k + 210k + 190k)
        assertEquals(290000.0, tierBudgets.get(3)); // LeadDev + SrDev (160k + 130k)

        String chainReport = service.generateChainOfCommandReport(tree);
        assertTrue(chainReport.contains("[ROOT]"));
        assertTrue(chainReport.contains("Alice Smith"));

        String tierReport = service.generateTierLevelReport(tree);
        assertTrue(tierReport.contains("TIER LEVEL 0"));
        assertTrue(tierReport.contains("TIER LEVEL 3"));
    }

    @Test
    @DisplayName("Default iterable on OrganizationTree returns depth-first iterator")
    void testDefaultIterable() {
        OrgIterator iterator = tree.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("E-100", iterator.next().getId());
    }
}
