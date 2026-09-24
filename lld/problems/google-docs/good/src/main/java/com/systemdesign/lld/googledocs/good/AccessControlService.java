package com.systemdesign.lld.googledocs.good;

/*
 * DESIGN INTENT:
 * Single Responsibility Principle (SRP).
 * Centralizes permission verification logic, isolating authorization rules from document manipulation.
 */
public class AccessControlService {

    public void validateReadAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role == null) {
            throw new SecurityException("User '" + userId + "' does not have read access to document '" + document.getId() + "'");
        }
    }

    public void validateWriteAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role != Role.OWNER && role != Role.EDITOR) {
            throw new SecurityException("User '" + userId + "' does not have write access to document '" + document.getId() + "'");
        }
    }

    public void validateOwnerAccess(Document document, String userId) {
        Role role = document.getRole(userId);
        if (role != Role.OWNER) {
            throw new SecurityException("User '" + userId + "' is not the OWNER of document '" + document.getId() + "'");
        }
    }
}
