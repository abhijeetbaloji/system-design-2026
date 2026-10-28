package com.systemdesign.lld.patterns.proxy.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Protection Proxy enforcing Role-Based Access Control and recording audit trails.
 */
public class DocumentAccessProxy implements Document {
    private final RealDocument realDocument;
    private final List<String> auditLog = new ArrayList<>();

    public DocumentAccessProxy(RealDocument realDocument) {
        this.realDocument = Objects.requireNonNull(realDocument, "RealDocument cannot be null");
    }

    @Override
    public String getId() {
        return realDocument.getId();
    }

    @Override
    public String getTitle() {
        return realDocument.getTitle();
    }

    @Override
    public String read(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (user.getRole() == Role.GUEST) {
            logViolation(user, "READ");
            throw new AccessDeniedException("GUEST user '" + user.getUsername() + "' is denied read access to: " + getId());
        }
        logSuccess(user, "READ");
        return realDocument.read(user);
    }

    @Override
    public void write(User user, String newContent) {
        Objects.requireNonNull(user, "User cannot be null");
        if (user.getRole() == Role.GUEST || user.getRole() == Role.VIEWER) {
            logViolation(user, "WRITE");
            throw new AccessDeniedException("User '" + user.getUsername() + "' with role " + user.getRole() + " is denied write access to: " + getId());
        }
        logSuccess(user, "WRITE");
        realDocument.write(user, newContent);
    }

    @Override
    public boolean delete(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (user.getRole() != Role.ADMIN) {
            logViolation(user, "DELETE");
            throw new AccessDeniedException("Only ADMINs can delete documents. User '" + user.getUsername() + "' [" + user.getRole() + "] denied.");
        }
        logSuccess(user, "DELETE");
        return realDocument.delete(user);
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    private void logSuccess(User user, String action) {
        String entry = String.format("[AUDIT SUCCESS] User '%s' [%s] performed %s on document '%s'",
                user.getUsername(), user.getRole(), action, getId());
        auditLog.add(entry);
        System.out.println(entry);
    }

    private void logViolation(User user, String action) {
        String entry = String.format("[AUDIT VIOLATION] User '%s' [%s] attempted unauthorized %s on document '%s'",
                user.getUsername(), user.getRole(), action, getId());
        auditLog.add(entry);
        System.err.println(entry);
    }
}
