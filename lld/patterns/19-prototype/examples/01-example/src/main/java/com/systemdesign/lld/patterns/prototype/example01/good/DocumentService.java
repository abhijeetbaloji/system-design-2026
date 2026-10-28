package com.systemdesign.lld.patterns.prototype.example01.good;

import java.util.Objects;

/**
 * Service demonstrating retrieval, specialization, and emission of cloned documents.
 */
public class DocumentService {
    private final DocumentPrototypeRegistry registry;

    public DocumentService(DocumentPrototypeRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "Registry cannot be null");
    }

    public InvoiceDocument createInvoiceForCustomer(String templateKey, String invoiceNumber, String customerName) {
        Document cloned = registry.get(templateKey);
        if (!(cloned instanceof InvoiceDocument invoice)) {
            throw new IllegalArgumentException("Template is not an invoice: " + templateKey);
        }
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCustomerName(customerName);
        return invoice;
    }
}
