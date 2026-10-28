package com.systemdesign.lld.patterns.cor.example02.bad;

import java.math.BigDecimal;

public class BadApprovalDemo {
    public static void main(String[] args) {
        BadApprovalManager manager = new BadApprovalManager();

        PurchaseRequest r1 = new PurchaseRequest("REQ-1", "Monitor cables", BigDecimal.valueOf(350), "Alice");
        PurchaseRequest r2 = new PurchaseRequest("REQ-2", "New developer laptops", BigDecimal.valueOf(18500), "Bob");

        System.out.println(manager.approve(r1));
        System.out.println(manager.approve(r2));
    }
}
