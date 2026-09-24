package com.systemdesign.lld.googledocs.security;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.Role;

/*
 * DESIGN INTENT:
 * Security Validator for Role-Based Access Control (RBAC).
 * Decouples authorization rules from editing algorithms and session synchronization.
 */
public class PermissionValidator {

    public void checkReadAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role == null) {
            throw new SecurityException("User '" + userId + "' does not have read access to document '" + document.getId() + "'");
        }
    }

    public void checkWriteAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role != Role.OWNER && role != Role.EDITOR) {
            throw new SecurityException("User '" + userId + "' does not have write access to document '" + document.getId() + "'");
        }
    }

    public void checkOwnerAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role != Role.OWNER) {
            throw new SecurityException("User '" + userId + "' is not the OWNER of document '" + document.getId() + "'");
        }
    }
}
