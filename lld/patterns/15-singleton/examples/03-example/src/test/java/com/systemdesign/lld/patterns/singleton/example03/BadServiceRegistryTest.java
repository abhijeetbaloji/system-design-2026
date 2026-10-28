package com.systemdesign.lld.patterns.singleton.example03;

import com.systemdesign.lld.patterns.singleton.example03.bad.BadServiceRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Independent Registry Instances Tests")
class BadServiceRegistryTest {

    @Test
    @DisplayName("Services registered in one BadServiceRegistry are not visible in another")
    void testServiceVisibilityFailure() {
        BadServiceRegistry reg1 = new BadServiceRegistry();
        BadServiceRegistry reg2 = new BadServiceRegistry();

        assertNotSame(reg1, reg2);

        reg1.register("db", "PostgresDriver");
        assertNotNull(reg1.resolve("db"));
        assertNull(reg2.resolve("db")); // reg2 fails to discover service
    }
}
