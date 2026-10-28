package com.systemdesign.lld.patterns.proxy.example02.good;

public class DocumentDemo {
    public static void main(String[] args) {
        RealDocument confidentialDoc = new RealDocument("DOC-2026-X", "Project Titan Budget", "Confidential: $120M allocated for quantum research.");
        Document secureDocument = new DocumentAccessProxy(confidentialDoc);

        User guest = new User("guest_bot", Role.GUEST);
        User viewer = new User("intern_sam", Role.VIEWER);
        User manager = new User("manager_sarah", Role.MANAGER);
        User admin = new User("ciso_alex", Role.ADMIN);

        System.out.println("=== Testing Role-Based Protection Proxy ===");

        // 1. Viewer reads
        System.out.println("1. Viewer reading: " + secureDocument.read(viewer));

        // 2. Viewer attempts edit (denied)
        try {
            secureDocument.write(viewer, "Hacked content");
        } catch (AccessDeniedException e) {
            System.out.println("1b. Expected denial: " + e.getMessage());
        }

        // 3. Manager edits document (allowed)
        secureDocument.write(manager, "Revised: $135M allocated for quantum research.");
        System.out.println("2. Manager updated: " + secureDocument.read(manager));

        // 4. Manager attempts delete (denied)
        try {
            secureDocument.delete(manager);
        } catch (AccessDeniedException e) {
            System.out.println("2b. Expected denial: " + e.getMessage());
        }

        // 5. Admin deletes document (allowed)
        secureDocument.delete(admin);
        System.out.println("3. Admin deleted successfully.");
    }
}
