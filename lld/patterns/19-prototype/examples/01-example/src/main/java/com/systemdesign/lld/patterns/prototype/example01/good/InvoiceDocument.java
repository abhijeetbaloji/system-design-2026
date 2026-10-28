package com.systemdesign.lld.patterns.prototype.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Prototype representing a commercial invoice.
 * Implements thorough deep copying of the line items list and child items.
 */
public class InvoiceDocument extends Document {
    private String invoiceNumber;
    private String customerName;
    private double taxRate;
    private final List<LineItem> items = new ArrayList<>();

    public InvoiceDocument(String title, String author, double taxRate) {
        super(title, author);
        if (taxRate < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }
        this.taxRate = taxRate;
        this.invoiceNumber = "DRAFT";
        this.customerName = "UNASSIGNED";
    }

    protected InvoiceDocument(InvoiceDocument source) {
        super(source); // Copies title, author, and metadata map
        this.invoiceNumber = source.invoiceNumber;
        this.customerName = source.customerName;
        this.taxRate = source.taxRate;

        // Strict Deep Copy of all line items
        for (LineItem item : source.items) {
            this.items.add(item.clone());
        }
    }

    @Override
    public InvoiceDocument clone() {
        return new InvoiceDocument(this);
    }

    public void addItem(LineItem item) {
        items.add(Objects.requireNonNull(item, "Line item cannot be null"));
    }

    public double calculateSubtotal() {
        return items.stream().mapToDouble(LineItem::getTotal).sum();
    }

    public double calculateTax() {
        return calculateSubtotal() * taxRate;
    }

    public double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = Objects.requireNonNull(invoiceNumber);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = Objects.requireNonNull(customerName);
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public List<LineItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String formatSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== INVOICE: %s ===\n", invoiceNumber));
        sb.append(String.format("Customer: %s | Author: %s\n", customerName, author));
        sb.append(String.format("Metadata: %s\n", metadata));
        sb.append("Items:\n");
        for (LineItem item : items) {
            sb.append(" - ").append(item).append("\n");
        }
        sb.append(String.format("Subtotal: $%,.2f | Tax (%.1f%%): $%,.2f | Total: $%,.2f\n",
                calculateSubtotal(), taxRate * 100, calculateTax(), calculateTotal()));
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatSummary();
    }
}
