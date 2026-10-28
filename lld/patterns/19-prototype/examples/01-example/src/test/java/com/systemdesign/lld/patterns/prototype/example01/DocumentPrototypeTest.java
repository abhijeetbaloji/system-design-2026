package com.systemdesign.lld.patterns.prototype.example01;

import com.systemdesign.lld.patterns.prototype.example01.good.Document;
import com.systemdesign.lld.patterns.prototype.example01.good.DocumentPrototypeRegistry;
import com.systemdesign.lld.patterns.prototype.example01.good.DocumentService;
import com.systemdesign.lld.patterns.prototype.example01.good.InvoiceDocument;
import com.systemdesign.lld.patterns.prototype.example01.good.LineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Document Prototype Pattern Tests")
class DocumentPrototypeTest {

    private DocumentPrototypeRegistry registry;
    private InvoiceDocument goldenTemplate;

    @BeforeEach
    void setUp() {
        registry = new DocumentPrototypeRegistry();

        goldenTemplate = new InvoiceDocument("SaaS Retainer", "Billing Team", 0.10);
        goldenTemplate.addMetadata("Currency", "USD");
        goldenTemplate.addMetadata("Region", "US-East");
        goldenTemplate.addItem(new LineItem("Cloud Infrastructure Retainer", 1, 1000.00));
        goldenTemplate.addItem(new LineItem("Standard SLA Support", 1, 200.00));

        registry.register("retainer-invoice", goldenTemplate);
    }

    @Test
    @DisplayName("Registry returns a distinct cloned instance rather than the cached reference")
    void testRegistryCloning() {
        Document clone1 = registry.get("retainer-invoice");
        Document clone2 = registry.get("retainer-invoice");

        assertNotSame(goldenTemplate, clone1);
        assertNotSame(clone1, clone2);
        assertEquals(goldenTemplate.getTitle(), clone1.getTitle());
    }

    @Test
    @DisplayName("Deep copy guarantees line item mutations in clone do not pollute prototype")
    void testDeepCopyLineItemIsolation() {
        InvoiceDocument invoiceA = (InvoiceDocument) registry.get("retainer-invoice");
        invoiceA.setCustomerName("Acme Corp");
        invoiceA.addItem(new LineItem("Custom Integration", 5, 200.00)); // 5 * 200 = 1000

        // Invoice A should have 3 items: 1000 + 200 + 1000 = 2200
        assertEquals(3, invoiceA.getItems().size());
        assertEquals(2200.00, invoiceA.calculateSubtotal(), 0.001);

        // Golden template in registry MUST remain at 2 items: 1000 + 200 = 1200
        assertEquals(2, goldenTemplate.getItems().size());
        assertEquals(1200.00, goldenTemplate.calculateSubtotal(), 0.001);

        // A second clone must also be pristine
        InvoiceDocument invoiceB = (InvoiceDocument) registry.get("retainer-invoice");
        assertEquals(2, invoiceB.getItems().size());
    }

    @Test
    @DisplayName("Deep copy guarantees metadata map mutations do not pollute prototype")
    void testMetadataMapIsolation() {
        InvoiceDocument clone = (InvoiceDocument) registry.get("retainer-invoice");
        clone.addMetadata("CustomTag", "VIP-Client");

        assertTrue(clone.getMetadata().containsKey("CustomTag"));
        assertFalse(goldenTemplate.getMetadata().containsKey("CustomTag"));
    }

    @Test
    @DisplayName("Invoice calculations are computed accurately")
    void testFinancialCalculations() {
        InvoiceDocument clone = (InvoiceDocument) registry.get("retainer-invoice");
        // Subtotal = 1000 + 200 = 1200.00
        // Tax 10% = 120.00
        // Total = 1320.00
        assertEquals(1200.00, clone.calculateSubtotal(), 0.001);
        assertEquals(120.00, clone.calculateTax(), 0.001);
        assertEquals(1320.00, clone.calculateTotal(), 0.001);
    }

    @Test
    @DisplayName("DocumentService specializes cloned invoice fields correctly")
    void testDocumentService() {
        DocumentService service = new DocumentService(registry);
        InvoiceDocument invoice = service.createInvoiceForCustomer("retainer-invoice", "INV-101", "Tesla Inc");

        assertEquals("INV-101", invoice.getInvoiceNumber());
        assertEquals("Tesla Inc", invoice.getCustomerName());
        assertEquals(2, invoice.getItems().size());
    }

    @Test
    @DisplayName("Registry throws NoSuchElementException for unregistered keys")
    void testUnknownKeyThrows() {
        assertThrows(NoSuchElementException.class, () -> registry.get("non-existent-template"));
    }
}
