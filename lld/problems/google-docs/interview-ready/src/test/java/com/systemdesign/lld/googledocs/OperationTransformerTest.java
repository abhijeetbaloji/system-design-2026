package com.systemdesign.lld.googledocs;

import com.systemdesign.lld.googledocs.operation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTransformerTest {

    private OperationTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new OperationTransformer();
    }

    @Test
    void testConcurrentInsertVsInsertDifferentPositions() {
        // Base text: "Hello World"
        // Client A inserts "Big " at position 6 (baseRev=0)
        // Server already committed Client B inserting "Dear " at position 0 (baseRev=0)
        InsertOperation clientOp = new InsertOperation("alice", 0, 6, "Big ");
        InsertOperation serverOp = new InsertOperation("bob", 0, 0, "Dear ");

        Operation transformed = transformer.transform(clientOp, serverOp);
        assertTrue(transformed instanceof InsertOperation);
        InsertOperation ins = (InsertOperation) transformed;

        // Position 6 should shift by length of "Dear " (5) -> 11
        assertEquals(11, ins.getPosition());
        assertEquals("Big ", ins.getText());
    }

    @Test
    void testConcurrentInsertVsInsertSamePositionTieBreaker() {
        // Both insert at position 0. Alice vs Bob.
        // "alice".compareTo("bob") < 0, so Alice keeps position 0.
        InsertOperation aliceOp = new InsertOperation("alice", 0, 0, "A");
        InsertOperation bobOp = new InsertOperation("bob", 0, 0, "B");

        Operation transformedAlice = transformer.transform(aliceOp, bobOp);
        assertEquals(0, transformedAlice.getPosition());

        // Bob transforming against Alice shifts right
        Operation transformedBob = transformer.transform(bobOp, aliceOp);
        assertEquals(1, transformedBob.getPosition());
    }

    @Test
    void testConcurrentInsertVsDelete() {
        // Server deleted 5 characters from index 2 to 7
        DeleteOperation serverDel = new DeleteOperation("bob", 0, 2, 5);

        // Client inserts at position 10 (after delete range)
        InsertOperation clientAfter = new InsertOperation("alice", 0, 10, "XYZ");
        Operation transformedAfter = transformer.transform(clientAfter, serverDel);
        assertEquals(5, transformedAfter.getPosition()); // 10 - 5 = 5

        // Client inserts at position 0 (before delete range)
        InsertOperation clientBefore = new InsertOperation("alice", 0, 0, "XYZ");
        Operation transformedBefore = transformer.transform(clientBefore, serverDel);
        assertEquals(0, transformedBefore.getPosition());
    }

    @Test
    void testConcurrentDeleteVsInsert() {
        // Client wants to delete 4 chars at pos 5
        DeleteOperation clientDel = new DeleteOperation("alice", 0, 5, 4);

        // Server inserted 3 chars at pos 2 (before delete)
        InsertOperation serverIns = new InsertOperation("bob", 0, 2, "ABC");
        Operation transformed = transformer.transform(clientDel, serverIns);

        assertEquals(8, transformed.getPosition()); // 5 + 3 = 8
        assertEquals(4, ((DeleteOperation) transformed).getLength());
    }

    @Test
    void testConcurrentDeleteVsDeleteNonOverlapping() {
        // Client deletes at 10, len 3
        DeleteOperation clientDel = new DeleteOperation("alice", 0, 10, 3);
        // Server deletes at 2, len 4
        DeleteOperation serverDel = new DeleteOperation("bob", 0, 2, 4);

        Operation transformed = transformer.transform(clientDel, serverDel);
        assertEquals(6, transformed.getPosition()); // 10 - 4 = 6
        assertEquals(3, ((DeleteOperation) transformed).getLength());
    }
}
