package com.systemdesign.lld.patterns.singleton.example01;

import com.systemdesign.lld.patterns.singleton.example01.good.AppConfigSingleton;
import com.systemdesign.lld.patterns.singleton.example01.good.OrderService;
import com.systemdesign.lld.patterns.singleton.example01.good.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Bill Pugh AppConfig Singleton Tests")
class AppConfigSingletonTest {

    @BeforeEach
    void setUp() {
        AppConfigSingleton.resetDefaultsForTesting();
    }

    @Test
    @DisplayName("Multiple calls to getInstance() must return the exact same reference")
    void testSingletonIdentity() {
        AppConfigSingleton instance1 = AppConfigSingleton.getInstance();
        AppConfigSingleton instance2 = AppConfigSingleton.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    @DisplayName("Services sharing singleton observe identical synchronized updates")
    void testSharedStateSynchronization() {
        AppConfigSingleton config = AppConfigSingleton.getInstance();
        OrderService orderService = new OrderService(config);
        PaymentService paymentService = new PaymentService(config);

        assertEquals(100, orderService.getMaxAllowedOrders());
        assertEquals("https://api.stripe.com/v1", paymentService.getGatewayEndpoint());

        config.setProperty("max.order.limit", "750");
        assertEquals(750, orderService.getMaxAllowedOrders());
    }

    @Test
    @DisplayName("Deserialization should return the exact same singleton instance")
    void testSerializationDefense() throws IOException, ClassNotFoundException {
        AppConfigSingleton original = AppConfigSingleton.getInstance();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(original);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        AppConfigSingleton deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            deserialized = (AppConfigSingleton) ois.readObject();
        }

        assertSame(original, deserialized);
    }

    @Test
    @DisplayName("Reflection instantiation attempt should be blocked with IllegalStateException")
    void testReflectionDefense() {
        // Ensure singleton is already initialized
        AppConfigSingleton.getInstance();

        Constructor<?>[] constructors = AppConfigSingleton.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
        assertTrue(thrown.getCause().getMessage().contains("Reflection instantiation is prohibited"));
    }

    @Test
    @DisplayName("Configuration fallback and boolean parsing works properly")
    void testPropertyAccessors() {
        AppConfigSingleton config = AppConfigSingleton.getInstance();

        assertEquals("fallbackVal", config.getString("non.existent.key", "fallbackVal"));
        assertEquals(999, config.getInt("non.existent.key", 999));
        assertTrue(config.getBoolean("feature.instant_checkout", false));
        assertFalse(config.getBoolean("non.existent.bool", false));
        assertTrue(config.contains("app.name"));
        assertFalse(config.contains("unknown.key"));
    }
}
