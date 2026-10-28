# Document Access Control Protection Proxy Example

## 1. Scenario Overview

In corporate content management systems (like Microsoft SharePoint, Google Workspace, or Confluence), documents contain confidential information (e.g., quarterly financial forecasts, acquisition term sheets, salary benchmarks).
- **Security Invariants**:
  - `GUEST`: Denied all access.
  - `VIEWER`: Can read documents, but cannot write or delete.
  - `MANAGER`: Can read and update document contents, but cannot delete.
  - `ADMIN`: Full access (read, write, delete).
- In addition, compliance frameworks (SOC 2, ISO 27001) require that every access attempt (both permitted and rejected) be recorded in an audit trail.

A **Protection Proxy** (`DocumentAccessProxy`) intercepts all interactions with the underlying `RealDocument`. It validates the user's role, logs the access attempt, and throws an `AccessDeniedException` if unauthorized—keeping the `RealDocument` free of security boilerplate.

---

## 2. Bad Design vs Good Design

### Bad Design (Tangling Security in Domain Objects)
- `BadDocument` directly embeds `if (user.getRole() == ...)` blocks inside its `read()`, `write()`, and `delete()` methods.
- Modifying security policies (e.g., adding an `AUDITOR` role) requires editing the document storage class directly.
- The domain class is bloated with security checks, exception handling, and audit logging.

### Good Design (Protection Proxy Pattern)
- `Document` defines the common subject contract (`read`, `write`, `delete`).
- `RealDocument` focuses strictly on storing and mutating document state.
- `DocumentAccessProxy` enforces Role-Based Access Control (RBAC), records security audit logs, and gates access to `RealDocument`.
- Security policies can be enhanced or replaced independently of document storage.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.proxy.example02
├── bad
│   ├── BadDocument.java
│   ├── BadDocumentDemo.java
│   └── User.java
└── good
    ├── AccessDeniedException.java
    ├── Document.java
    ├── DocumentAccessProxy.java
    ├── DocumentDemo.java
    ├── RealDocument.java
    ├── Role.java
    └── User.java
```

---

## 4. Execution Flow & SOLID Principles

- **Security Gatekeeping**: When a client calls `document.write(user, content)`, the `DocumentAccessProxy` evaluates `user.getRole()`. If authorized (`MANAGER` or `ADMIN`), it delegates to `realDocument.write()` and records a success entry in the audit trail. If unauthorized, it records a violation entry and raises `AccessDeniedException`.
- **Single Responsibility Principle (SRP)**: `RealDocument` manages document content; `DocumentAccessProxy` manages authorization and audit trails.
- **Open/Closed Principle (OCP)**: New security policies, rate-limiting rules, or encryption decorators can be added without altering `RealDocument`.
