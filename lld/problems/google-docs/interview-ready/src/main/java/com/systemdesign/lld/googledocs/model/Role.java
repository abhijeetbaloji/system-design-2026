package com.systemdesign.lld.googledocs.model;

/*
 * DESIGN INTENT:
 * Explicit Enum for Role-Based Access Control (RBAC).
 * Enforces role boundaries across viewers, editors, and owners.
 */
public enum Role {
    OWNER,
    EDITOR,
    VIEWER
}
