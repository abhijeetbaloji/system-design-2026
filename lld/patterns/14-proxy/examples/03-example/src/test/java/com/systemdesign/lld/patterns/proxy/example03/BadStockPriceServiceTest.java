package com.systemdesign.lld.patterns.proxy.example03;

import com.systemdesign.lld.patterns.proxy.example03.bad.BadStockPriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bad Design: Redundant Network Calls Stock Service Tests")
class BadStockPriceServiceTest {

    @Test
    @DisplayName("BadStockPriceService makes duplicate remote network calls for every repeated request")
    void testRedundantNetworkCalls() {
        BadStockPriceService service = new BadStockPriceService();

        double p1 = service.fetchPrice("AAPL");
        double p2 = service.fetchPrice("AAPL");
        double p3 = service.fetchPrice("AAPL");

        assertEquals(182.50, p1, 0.001);
        assertEquals(p1, p2);
        assertEquals(p2, p3);

        // Demonstrates problem: 3 calls made for 3 identical reads
        assertEquals(3, service.getNetworkCalls());
    }
}
