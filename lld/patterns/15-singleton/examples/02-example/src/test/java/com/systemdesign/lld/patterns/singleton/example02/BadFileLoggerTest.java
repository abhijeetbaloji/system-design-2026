package com.systemdesign.lld.patterns.singleton.example02;

import com.systemdesign.lld.patterns.singleton.example02.bad.BadFileLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Isolated File Logger Tests")
class BadFileLoggerTest {

    @Test
    @DisplayName("BadFileLogger instances maintain isolated and unshared buffers")
    void testIsolatedBuffers() {
        BadFileLogger logger1 = new BadFileLogger("ModuleA");
        BadFileLogger logger2 = new BadFileLogger("ModuleB");

        assertNotSame(logger1, logger2);

        logger1.log("INFO", "Message A");
        assertEquals(1, logger1.getLocalBuffer().size());
        assertEquals(0, logger2.getLocalBuffer().size()); // Module B never sees Module A's logs
    }
}
