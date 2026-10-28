package com.systemdesign.lld.patterns.singleton.example01;

import com.systemdesign.lld.patterns.singleton.example01.bad.BadConfigurationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Multiple Uncoordinated Config Instances Tests")
class BadConfigurationTest {

    @Test
    @DisplayName("BadConfigurationManager creates distinct instances with divergent state")
    void testDivergentInstances() {
        BadConfigurationManager config1 = new BadConfigurationManager();
        BadConfigurationManager config2 = new BadConfigurationManager();

        assertNotSame(config1, config2);

        config1.set("max.order.limit", "999");
        assertEquals("999", config1.get("max.order.limit"));
        assertEquals("100", config2.get("max.order.limit")); // config2 did not get the update!
    }
}
