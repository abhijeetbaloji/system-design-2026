package com.systemdesign.lld.patterns.facade.example02.bad;

import java.math.BigDecimal;

public record CartItem(String sku, int quantity, BigDecimal unitPrice) {}
