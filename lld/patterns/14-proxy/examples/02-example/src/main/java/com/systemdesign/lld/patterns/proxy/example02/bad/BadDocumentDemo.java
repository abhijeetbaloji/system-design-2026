package com.systemdesign.lld.patterns.proxy.example02.bad;

public class BadDocumentDemo {
    public static void main(String[] args) {
        BadDocument doc = new BadDocument("DOC-101", "Q3 Earnings", "Revenue: $42M, Net Profit: $8M");

        User viewer = new User("alice", "VIEWER");
        User guest = new User("bob", "GUEST");

        System.out.println("Viewer reading: " + doc.read(viewer));

        try {
            doc.read(guest);
        } catch (SecurityException e) {
            System.out.println("Blocked guest: " + e.getMessage());
        }
    }
}
