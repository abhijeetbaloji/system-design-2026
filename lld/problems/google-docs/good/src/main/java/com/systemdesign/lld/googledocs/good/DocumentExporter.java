package com.systemdesign.lld.googledocs.good;

/*
 * DESIGN INTENT:
 * Strategy Pattern / Open-Closed Principle (OCP).
 * Enables adding new export targets without modifying the Document class.
 */
public interface DocumentExporter {

    String export(Document document);
}
