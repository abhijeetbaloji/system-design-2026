package com.systemdesign.lld.patterns.prototype.example01.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * Flawed design: Performs shallow copy of list and item references.
 */
public class BadInvoice {
    public String customerName;
    public List<BadLineItem> items;

    public BadInvoice(String customerName, List<BadLineItem> items) {
        this.customerName = customerName;
        this.items = items;
    }

    // Flawed: Shallow copy copies only pointer to original list!
    public BadInvoice shallowCopy() {
        return new BadInvoice(this.customerName, this.items);
    }
}
