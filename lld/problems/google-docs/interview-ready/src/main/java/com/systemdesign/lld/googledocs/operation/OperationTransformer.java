package com.systemdesign.lld.googledocs.operation;

/*
 * DESIGN INTENT:
 * Strategy Pattern / Transformation Engine for Operational Transformation (OT).
 * Transforms a client-submitted operation against a concurrently committed server operation.
 * Guarantees convergence across distributed collaborators without data clobbering.
 */
public class OperationTransformer {

    /*
     * Transforms clientOp given that serverOp has already been committed.
     * Returns a new transformed Operation that can be safely applied to the server's current state.
     */
    public Operation transform(Operation clientOp, Operation serverOp) {
        if (clientOp instanceof InsertOperation clientIns && serverOp instanceof InsertOperation serverIns) {
            return transformInsertVsInsert(clientIns, serverIns);
        } else if (clientOp instanceof InsertOperation clientIns && serverOp instanceof DeleteOperation serverDel) {
            return transformInsertVsDelete(clientIns, serverDel);
        } else if (clientOp instanceof DeleteOperation clientDel && serverOp instanceof InsertOperation serverIns) {
            return transformDeleteVsInsert(clientDel, serverIns);
        } else if (clientOp instanceof DeleteOperation clientDel && serverOp instanceof DeleteOperation serverDel) {
            return transformDeleteVsDelete(clientDel, serverDel);
        } else if (clientOp instanceof FormatOperation clientFmt && serverOp instanceof InsertOperation serverIns) {
            return transformFormatVsInsert(clientFmt, serverIns);
        } else if (clientOp instanceof FormatOperation clientFmt && serverOp instanceof DeleteOperation serverDel) {
            return transformFormatVsDelete(clientFmt, serverDel);
        }
        // If unhandled combination or formatting vs formatting, return client op
        return clientOp;
    }

    private Operation transformInsertVsInsert(InsertOperation c, InsertOperation s) {
        int cPos = c.getPosition();
        int sPos = s.getPosition();
        int sLen = s.getLength();

        if (cPos < sPos) {
            return c;
        } else if (cPos > sPos) {
            return c.withPosition(cPos + sLen);
        } else {
            // Deterministic tie-breaker: author ID comparison
            if (c.getAuthorId().compareTo(s.getAuthorId()) < 0) {
                return c;
            } else {
                return c.withPosition(cPos + sLen);
            }
        }
    }

    private Operation transformInsertVsDelete(InsertOperation c, DeleteOperation s) {
        int cPos = c.getPosition();
        int sPos = s.getPosition();
        int sLen = s.getLength();

        if (cPos <= sPos) {
            return c;
        } else if (cPos >= sPos + sLen) {
            return c.withPosition(cPos - sLen);
        } else {
            // Insert happened inside the deleted region: snap to start of deletion
            return c.withPosition(sPos);
        }
    }

    private Operation transformDeleteVsInsert(DeleteOperation c, InsertOperation s) {
        int cPos = c.getPosition();
        int cLen = c.getLength();
        int sPos = s.getPosition();
        int sLen = s.getLength();

        if (sPos >= cPos + cLen) {
            // Insert is after delete range
            return c;
        } else if (sPos <= cPos) {
            // Insert is before delete range
            return c.withPosition(cPos + sLen);
        } else {
            // Insert is strictly inside delete range -> expand deletion to consume inserted text
            return c.withPositionAndLength(cPos, cLen + sLen);
        }
    }

    private Operation transformDeleteVsDelete(DeleteOperation c, DeleteOperation s) {
        int cStart = c.getPosition();
        int cEnd = cStart + c.getLength();
        int sStart = s.getPosition();
        int sEnd = sStart + s.getLength();

        if (cEnd <= sStart) {
            // Client delete is strictly before server delete
            return c;
        } else if (cStart >= sEnd) {
            // Client delete is strictly after server delete
            return c.withPosition(cStart - s.getLength());
        } else {
            // Overlapping deletions
            int overlapStart = Math.max(cStart, sStart);
            int overlapEnd = Math.min(cEnd, sEnd);
            int overlapLen = overlapEnd - overlapStart;

            int newLen = c.getLength() - overlapLen;
            if (newLen <= 0) {
                // Entire client delete was already deleted by server
                return new DeleteOperation(c.getAuthorId(), c.getBaseRevision(), cStart, 0);
            }

            int newStart = cStart;
            if (cStart >= sStart) {
                newStart = sStart;
            }
            return c.withPositionAndLength(newStart, newLen);
        }
    }

    private Operation transformFormatVsInsert(FormatOperation c, InsertOperation s) {
        int cPos = c.getPosition();
        int cLen = c.getLength();
        int sPos = s.getPosition();
        int sLen = s.getLength();

        if (sPos <= cPos) {
            return c.withPosition(cPos + sLen);
        } else if (sPos < cPos + cLen) {
            return c.withPositionAndLength(cPos, cLen + sLen);
        }
        return c;
    }

    private Operation transformFormatVsDelete(FormatOperation c, DeleteOperation s) {
        int cPos = c.getPosition();
        int cLen = c.getLength();
        int sPos = s.getPosition();
        int sLen = s.getLength();

        if (cPos >= sPos + sLen) {
            return c.withPosition(cPos - sLen);
        } else if (sPos >= cPos + cLen) {
            return c;
        } else {
            int newStart = Math.min(cPos, sPos);
            int newLen = Math.max(0, cLen - sLen);
            return c.withPositionAndLength(newStart, newLen);
        }
    }
}
