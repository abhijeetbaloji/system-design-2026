package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;

public record CartItem(String sku, int quantity, BigDecimal unitPrice) {}
