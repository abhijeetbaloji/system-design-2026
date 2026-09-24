package com.systemdesign.lld.googledocs.domain.operation;

import com.systemdesign.lld.googledocs.domain.model.Document;

/*
 * DESIGN INTENT:
 * Command Pattern interface combined with Operational Transformation (OT) semantics.
 * Every operation carries author attribution and the baseline revision upon which it was authored.
 * Operations are reversible via invert(Document document).
 */
public interface Operation {

    String getAuthorId();

    int getBaseRevision();

    int getPosition();

    void apply(Document document);

    Operation invert(Document document);
}
