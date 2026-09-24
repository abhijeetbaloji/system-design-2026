package com.systemdesign.lld.googledocs.good;

/*
 * DESIGN INTENT:
 * Explicit Enum replaces magic string literals ("OWNER", "EDITOR", "VIEWER").
 * Strongly typed roles eliminate runtime typos and enforce compile-time verification.
 */
public enum Role {
    OWNER,
    EDITOR,
    VIEWER
}
