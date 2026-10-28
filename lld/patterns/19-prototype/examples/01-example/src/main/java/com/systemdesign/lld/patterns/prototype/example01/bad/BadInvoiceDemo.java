package com.systemdesign.lld.patterns.prototype.example01.bad;

import java.util.ArrayList;
import java.util.List;

public class BadInvoiceDemo {
    public static void main(String[] args) {
        List<BadLineItem> baseline = new ArrayList<>();
        baseline.add(new BadLineItem("Hosting Retainer", 500.0));

        BadInvoice template = new BadInvoice("Standard Template", baseline);

        // Client shallow copies template for Customer A
        BadInvoice invoiceA = template.shallowCopy();
        invoiceA.customerName = "Acme Corp";
        invoiceA.items.add(new BadLineItem("Extra Consulting", 2000.0));

        // Client shallow copies template for Customer B
        BadInvoice invoiceB = template.shallowCopy();
        invoiceB.customerName = "Beta LLC";

        System.out.println("Customer A items: " + invoiceA.items.size()); // 2
        System.out.println("Customer B items: " + invoiceB.items.size()); // BUG: 2! Contaminated by Customer A!
        System.out.println("Template items:   " + template.items.size()); // BUG: 2! Original template destroyed!
    }
}
