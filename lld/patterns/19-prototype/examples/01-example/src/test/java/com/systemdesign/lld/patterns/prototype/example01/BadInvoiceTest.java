package com.systemdesign.lld.patterns.prototype.example01;

import com.systemdesign.lld.patterns.prototype.example01.bad.BadInvoice;
import com.systemdesign.lld.patterns.prototype.example01.bad.BadLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Bad Invoice Shallow Copy Leak Tests")
class BadInvoiceTest {

    @Test
    @DisplayName("Shallow copy shares item collection reference leading to cross-contamination")
    void testShallowCopyLeak() {
        List<BadLineItem> list = new ArrayList<>();
        list.add(new BadLineItem("Base Item", 100.0));

        BadInvoice original = new BadInvoice("Original", list);
        BadInvoice clone = original.shallowCopy();

        // Flaw: Clone shares the exact same list reference
        assertSame(original.items, clone.items);

        // Mutating clone corrupts original!
        clone.items.add(new BadLineItem("Injected Item", 200.0));
        assertEquals(2, original.items.size(), "Original invoice was corrupted by clone mutation!");
    }
}
