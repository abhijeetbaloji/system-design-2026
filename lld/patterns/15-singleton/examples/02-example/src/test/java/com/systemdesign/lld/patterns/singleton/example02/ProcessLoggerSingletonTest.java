package com.systemdesign.lld.patterns.singleton.example02;

import com.systemdesign.lld.patterns.singleton.example02.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Double-Checked Locking ProcessLogger Tests")
class ProcessLoggerSingletonTest {

    private ProcessLoggerSingleton logger;

    @BeforeEach
    void setUp() {
        logger = ProcessLoggerSingleton.getInstance();
        logger.clearLogs();
        logger.setLogLevel(LogLevel.DEBUG);
    }

    @Test
    @DisplayName("Multiple calls to getInstance() must return the exact same reference")
    void testSingletonIdentity() {
        ProcessLoggerSingleton instance1 = ProcessLoggerSingleton.getInstance();
        ProcessLoggerSingleton instance2 = ProcessLoggerSingleton.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("Logger filters messages below the configured LogLevel threshold")
    void testLogLevelFiltering() {
        logger.setLogLevel(LogLevel.WARN);

        logger.debug("Debug msg - should be ignored");
        logger.info("Info msg - should be ignored");
        logger.warn("Warn msg - should be recorded");
        logger.error("Error msg - should be recorded");

        assertEquals(2, logger.getLogs().size());
        assertEquals(LogLevel.WARN, logger.getLogs().get(0).getLevel());
        assertEquals(LogLevel.ERROR, logger.getLogs().get(1).getLevel());
    }

    @Test
    @DisplayName("Multi-threaded logging safely appends entries without race conditions")
    void testConcurrentLogging() throws InterruptedException {
        int threadCount = 10;
        int logsPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int workerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < logsPerThread; j++) {
                        logger.info("Worker " + workerId + " message " + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threadCount * logsPerThread, logger.getLogs().size());
    }

    @Test
    @DisplayName("Deserialization returns the exact same singleton instance")
    void testSerializationDefense() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(logger);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ProcessLoggerSingleton deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            deserialized = (ProcessLoggerSingleton) ois.readObject();
        }

        assertSame(logger, deserialized);
    }

    @Test
    @DisplayName("Reflection instantiation is blocked with IllegalStateException")
    void testReflectionDefense() {
        Constructor<?>[] constructors = ProcessLoggerSingleton.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
        assertTrue(thrown.getCause().getMessage().contains("already instantiated"));
    }
}
