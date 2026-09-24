package com.systemdesign.lld.googledocs.good;

/*
 * DESIGN INTENT:
 * Command Pattern interface.
 * Each document editing action is encapsulated as an object holding the context necessary
 * to both execute and reverse (undo) the operation.
 * Eliminates full-state string cloning.
 */
public interface Operation {

    void execute(Document document);

    void undo(Document document);
}
