package com.systemdesign.lld.googledocs.bad;

import java.util.*;

/*
 * DESIGN INTENT (BAD DESIGN):
 * This class demonstrates a monolithic "God Class" pattern where a single manager handles:
 * 1. Document storage & lifecycle.
 * 2. Character-level editing via raw String concatenation and slicing.
 * 3. Primitive formatting injected directly into the text stream (e.g. <b> tags).
 * 4. Snapshot-based Undo mechanism storing entire document strings on every edit.
 * 5. String-based security/permission checks embedded in business logic.
 * 6. Hardcoded export formats (TXT, HTML, Markdown) using cascading if-else statements.
 * 7. Coarse-grained global synchronization that serializes all edits across all documents.
 */
public class BadGoogleDocManager {

    // Document ID -> Document raw text content
    private final Map<String, String> documentContents = new HashMap<>();

    // Document ID -> Document title
    private final Map<String, String> documentTitles = new HashMap<>();

    // Document ID -> (User ID -> Role String: "OWNER", "EDITOR", "VIEWER")
    private final Map<String, Map<String, String>> documentPermissions = new HashMap<>();

    // Document ID -> List of full text snapshots for Undo (Massive memory leak potential)
    private final Map<String, List<String>> undoSnapshots = new HashMap<>();

    /*
     * DESIGN FLAW: Global synchronization on the entire manager instance.
     * Editing document "doc-1" blocks another user from creating or editing "doc-2".
     */
    public synchronized void createDocument(String docId, String title, String ownerId) {
        if (documentContents.containsKey(docId)) {
            throw new IllegalArgumentException("Document already exists: " + docId);
        }
        documentContents.put(docId, "");
        documentTitles.put(docId, title);

        Map<String, String> permissions = new HashMap<>();
        permissions.put(ownerId, "OWNER");
        documentPermissions.put(docId, permissions);

        List<String> history = new ArrayList<>();
        undoSnapshots.put(docId, history);
    }

    /*
     * DESIGN FLAW: Raw string manipulation and embedded string role checking.
     * String slicing creates temporary String objects on every keystroke.
     */
    public synchronized void insertText(String docId, String userId, int position, String text) {
        verifyWriteAccess(docId, userId);

        String current = documentContents.get(docId);
        if (position < 0 || position > current.length()) {
            throw new IndexOutOfBoundsException("Invalid insert position: " + position);
        }

        // Save state before modification for undo
        undoSnapshots.get(docId).add(current);

        // String concatenation creates a completely new string instance
        String updated = current.substring(0, position) + text + current.substring(position);
        documentContents.put(docId, updated);
    }

    /*
     * DESIGN FLAW: Substring delete with no encapsulation of rich text or paragraphs.
     */
    public synchronized void deleteText(String docId, String userId, int start, int length) {
        verifyWriteAccess(docId, userId);

        String current = documentContents.get(docId);
        if (start < 0 || start + length > current.length() || length < 0) {
            throw new IndexOutOfBoundsException("Invalid delete range: start=" + start + ", length=" + length);
        }

        undoSnapshots.get(docId).add(current);

        String updated = current.substring(0, start) + current.substring(start + length);
        documentContents.put(docId, updated);
    }

    /*
     * DESIGN FLAW: Mixing presentation tags directly inside character content.
     * Slicing HTML tags like "<b>" into plain text corrupts character offsets for subsequent edits!
     */
    public synchronized void applyFormatting(String docId, String userId, int start, int length, String formatType) {
        verifyWriteAccess(docId, userId);

        String current = documentContents.get(docId);
        if (start < 0 || start + length > current.length()) {
            throw new IndexOutOfBoundsException("Invalid format range");
        }

        undoSnapshots.get(docId).add(current);

        String targetSpan = current.substring(start, start + length);
        String formattedSpan;
        if ("BOLD".equalsIgnoreCase(formatType)) {
            formattedSpan = "<b>" + targetSpan + "</b>";
        } else if ("ITALIC".equalsIgnoreCase(formatType)) {
            formattedSpan = "<i>" + targetSpan + "</i>";
        } else {
            formattedSpan = targetSpan; // Unknown format
        }

        String updated = current.substring(0, start) + formattedSpan + current.substring(start + length);
        documentContents.put(docId, updated);
    }

    /*
     * DESIGN FLAW: Snapshot undo pops the last full string from the list.
     * Highly inefficient in terms of memory O(M * S) where M is mutations and S is doc size.
     */
    public synchronized void undo(String docId, String userId) {
        verifyWriteAccess(docId, userId);

        List<String> history = undoSnapshots.get(docId);
        if (history != null && !history.isEmpty()) {
            String previous = history.remove(history.size() - 1);
            documentContents.put(docId, previous);
        }
    }

    /*
     * DESIGN FLAW: Violation of Open/Closed Principle (OCP).
     * Adding a new export format (e.g. PDF, LaTeX, RTF) requires modifying this monolithic class.
     */
    public synchronized String exportDocument(String docId, String format) {
        if (!documentContents.containsKey(docId)) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        String content = documentContents.get(docId);
        String title = documentTitles.get(docId);

        if ("TXT".equalsIgnoreCase(format)) {
            // Strip out tags naively
            return content.replaceAll("<[^>]*>", "");
        } else if ("MARKDOWN".equalsIgnoreCase(format)) {
            return "# " + title + "\n\n" + content
                    .replace("<b>", "**").replace("</b>", "**")
                    .replace("<i>", "*").replace("</i>", "*");
        } else if ("HTML".equalsIgnoreCase(format)) {
            return "<html><head><title>" + title + "</title></head><body><p>" + content + "</p></body></html>";
        } else {
            throw new UnsupportedOperationException("Unsupported format: " + format);
        }
    }

    public synchronized void shareDocument(String docId, String actorId, String targetUserId, String role) {
        verifyOwnerAccess(docId, actorId);
        documentPermissions.get(docId).put(targetUserId, role.toUpperCase());
    }

    public synchronized String getDocumentContent(String docId, String userId) {
        verifyReadAccess(docId, userId);
        return documentContents.get(docId);
    }

    /*
     * DESIGN FLAW: String-based access control with magic strings repeated across methods.
     */
    private void verifyWriteAccess(String docId, String userId) {
        Map<String, String> perms = documentPermissions.get(docId);
        if (perms == null) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        String role = perms.get(userId);
        if (role == null || (!role.equals("OWNER") && !role.equals("EDITOR"))) {
            throw new SecurityException("User " + userId + " does not have write access to document " + docId);
        }
    }

    private void verifyReadAccess(String docId, String userId) {
        Map<String, String> perms = documentPermissions.get(docId);
        if (perms == null) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        String role = perms.get(userId);
        if (role == null) {
            throw new SecurityException("User " + userId + " does not have read access to document " + docId);
        }
    }

    private void verifyOwnerAccess(String docId, String userId) {
        Map<String, String> perms = documentPermissions.get(docId);
        if (perms == null) {
            throw new IllegalArgumentException("Document not found: " + docId);
        }
        String role = perms.get(userId);
        if (role == null || !role.equals("OWNER")) {
            throw new SecurityException("User " + userId + " is not OWNER of document " + docId);
        }
    }
}
