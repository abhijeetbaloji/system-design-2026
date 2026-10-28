package com.systemdesign.lld.patterns.singleton.example03;

import com.systemdesign.lld.patterns.singleton.example03.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Enum Singleton Service Registry Tests")
class ServiceRegistrySingletonTest {

    private ServiceRegistry registry;

    @BeforeEach
    void setUp() {
        registry = ServiceRegistrySingleton.INSTANCE;
        registry.clear();
    }

    @Test
    @DisplayName("Enum singleton instance is strictly unique")
    void testEnumSingletonUniqueness() {
        ServiceRegistry ref1 = ServiceRegistrySingleton.INSTANCE;
        ServiceRegistry ref2 = ServiceRegistrySingleton.valueOf("INSTANCE");

        assertSame(ref1, ref2);
    }

    @Test
    @DisplayName("Typed service registration and resolution works as expected")
    void testTypedServiceRegistration() {
        StripeGateway stripe = new StripeGateway();
        registry.register(PaymentGateway.class, stripe);

        Optional<PaymentGateway> resolved = registry.resolve(PaymentGateway.class);
        assertTrue(resolved.isPresent());
        assertSame(stripe, resolved.get());

        // Unregistered type returns Optional.empty()
        Optional<NotificationGateway> missing = registry.resolve(NotificationGateway.class);
        assertFalse(missing.isPresent());
    }

    @Test
    @DisplayName("Named service registration and resolution works case-insensitively")
    void testNamedServiceRegistration() {
        SendGridNotification sendgrid = new SendGridNotification();
        registry.registerNamed("Mailer", sendgrid);

        Optional<Object> resolved = registry.resolveNamed("mailer");
        assertTrue(resolved.isPresent());
        assertSame(sendgrid, resolved.get());
    }

    @Test
    @DisplayName("CheckoutController resolves services and processes order successfully")
    void testCheckoutControllerIntegration() {
        registry.register(PaymentGateway.class, new StripeGateway());
        registry.register(NotificationGateway.class, new SendGridNotification());

        CheckoutController checkout = new CheckoutController(registry);
        String receipt = checkout.completeCheckout(89.50, "buyer@domain.com");

        assertNotNull(receipt);
        assertTrue(receipt.contains("Stripe charged $89.50"));
        assertTrue(receipt.contains("SendGrid email dispatched"));
    }

    @Test
    @DisplayName("Missing service in CheckoutController throws IllegalStateException")
    void testMissingServiceError() {
        // Register payment but omit notification
        registry.register(PaymentGateway.class, new StripeGateway());

        CheckoutController checkout = new CheckoutController(registry);
        assertThrows(IllegalStateException.class, () -> checkout.completeCheckout(50.00, "user@test.com"));
    }

    @Test
    @DisplayName("Enum types are natively immune to reflection instantiation attempts")
    void testReflectionImmunity() {
        Constructor<?>[] constructors = ServiceRegistrySingleton.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);

        // In Java, trying to reflectively instantiate an Enum throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> constructor.newInstance("FAKE", 1));
    }

    @Test
    @DisplayName("Serialization of Enum Singleton preserves identical reference")
    void testSerializationPreservation() throws IOException, ClassNotFoundException {
        ServiceRegistry original = ServiceRegistrySingleton.INSTANCE;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(original);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ServiceRegistry deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            deserialized = (ServiceRegistry) ois.readObject();
        }

        assertSame(original, deserialized);
    }
}
