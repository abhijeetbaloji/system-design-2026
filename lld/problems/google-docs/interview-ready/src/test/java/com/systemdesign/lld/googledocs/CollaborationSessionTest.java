package com.systemdesign.lld.googledocs;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.operation.CommittedOperation;
import com.systemdesign.lld.googledocs.operation.InsertOperation;
import com.systemdesign.lld.googledocs.operation.OperationTransformer;
import com.systemdesign.lld.googledocs.session.CollaborationSession;
import com.systemdesign.lld.googledocs.session.SessionEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollaborationSessionTest {

    private CollaborationSession session;
    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document("doc-1", "Design Patterns", "alice");
        OperationTransformer transformer = new OperationTransformer();
        session = new CollaborationSession(document, transformer);
    }

    @Test
    void testSequentialOperationsAdvanceRevision() {
        CommittedOperation op1 = session.submitOperation(new InsertOperation("alice", 0, 0, "Hello"));
        assertEquals(1, op1.getRevisionNumber());
        assertEquals("Hello", document.getText());

        CommittedOperation op2 = session.submitOperation(new InsertOperation("alice", 1, 5, " World"));
        assertEquals(2, op2.getRevisionNumber());
        assertEquals("Hello World", document.getText());
    }

    @Test
    void testConcurrentOperationTransformationConverges() {
        // Base state (rev 0): empty doc
        // User 1 (Alice) types "World" at pos 0 against rev 0
        session.submitOperation(new InsertOperation("alice", 0, 0, "World"));
        // Current state: "World", rev 1

        // User 2 (Bob) simultaneously sent edit against rev 0: insert "Hello " at pos 0
        // Bob's op has baseRevision=0, but server is at rev 1!
        InsertOperation bobOp = new InsertOperation("bob", 0, 0, "Hello ");
        session.submitOperation(bobOp);

        // Bob's op was transformed against Alice's op.
        // Because "alice" < "bob", Alice's text stayed at 0, Bob's got shifted to pos 5.
        assertEquals("WorldHello ", document.getText());
        assertEquals(2, document.getRevision());
    }

    @Test
    void testObserverReceivesCommittedEvents() {
        List<CommittedOperation> receivedEvents = new ArrayList<>();
        SessionEventListener listener = (docId, committedOp) -> receivedEvents.add(committedOp);

        session.addListener(listener);
        session.submitOperation(new InsertOperation("alice", 0, 0, "EventTest"));

        assertEquals(1, receivedEvents.size());
        assertEquals(1, receivedEvents.get(0).getRevisionNumber());
    }
}
