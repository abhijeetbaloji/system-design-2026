package com.systemdesign.lld.patterns.prototype.example01.good;

public class DocumentCloneDemo {
    public static void main(String[] args) {
        DocumentPrototypeRegistry registry = new DocumentPrototypeRegistry();

        // 1. Create and warm golden template
        InvoiceDocument saasTemplate = new InvoiceDocument("SaaS Subscription Invoice", "Finance Department", 0.10);
        saasTemplate.addMetadata("Currency", "USD");
        saasTemplate.addMetadata("Terms", "Net-30");
        saasTemplate.addItem(new LineItem("Enterprise Cloud Base License", 1, 1500.00));
        saasTemplate.addItem(new LineItem("24/7 Premium SLA Support", 1, 500.00));

        registry.register("saas-template", saasTemplate);

        DocumentService service = new DocumentService(registry);

        // 2. Clone for Customer Acme Corp
        InvoiceDocument acmeInvoice = service.createInvoiceForCustomer("saas-template", "INV-2026-001", "Acme Corporation");
        acmeInvoice.addItem(new LineItem("Custom Migration Hours", 10, 150.00));

        // 3. Clone for Customer Beta LLC
        InvoiceDocument betaInvoice = service.createInvoiceForCustomer("saas-template", "INV-2026-002", "Beta Industries");
        betaInvoice.addItem(new LineItem("Dedicated IP Addon", 2, 50.00));

        System.out.println("=== ACME INVOICE (Cloned & Customized) ===");
        System.out.println(acmeInvoice);

        System.out.println("=== BETA INVOICE (Cloned & Customized) ===");
        System.out.println(betaInvoice);

        System.out.println("=== GOLDEN TEMPLATE (Completely Unaltered) ===");
        System.out.println(saasTemplate);
    }
}
