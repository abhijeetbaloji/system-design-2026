package com.systemdesign.lld.patterns.adapter.example03.good;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Adapter Class:
 * Bridges the Target (AppLogger) and Adaptee (LegacyCustomLogger).
 *
 * Responsibilities:
 * 1. Maps method names (debug, info, warn, error) to integer severity codes (1, 2, 3, 4).
 * 2. Supplies a default or configured module tag.
 * 3. Serializes Throwables into string stack traces automatically for the legacy logger.
 */
public class LegacyLoggerAdapter implements AppLogger {

    private static final int SEVERITY_DEBUG = 1;
    private static final int SEVERITY_INFO = 2;
    private static final int SEVERITY_WARN = 3;
    private static final int SEVERITY_ERROR = 4;

    private final LegacyCustomLogger legacyLogger;
    private final String defaultTag;

    public LegacyLoggerAdapter(LegacyCustomLogger legacyLogger, String defaultTag) {
        if (legacyLogger == null) {
            throw new IllegalArgumentException("LegacyCustomLogger cannot be null");
        }
        this.legacyLogger = legacyLogger;
        this.defaultTag = (defaultTag != null && !defaultTag.isBlank()) ? defaultTag : "APP";
    }

    public LegacyLoggerAdapter(LegacyCustomLogger legacyLogger) {
        this(legacyLogger, "APP");
    }

    @Override
    public void debug(String message) {
        legacyLogger.writeLog(SEVERITY_DEBUG, message, defaultTag);
    }

    @Override
    public void info(String message) {
        legacyLogger.writeLog(SEVERITY_INFO, message, defaultTag);
    }

    @Override
    public void warn(String message) {
        legacyLogger.writeLog(SEVERITY_WARN, message, defaultTag);
    }

    @Override
    public void error(String message) {
        legacyLogger.writeLog(SEVERITY_ERROR, message, defaultTag);
    }

    @Override
    public void error(String message, Throwable throwable) {
        String payload = message;
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            payload = message + "\nException: " + throwable.getClass().getName() + ": " + throwable.getMessage() + "\n" + sw;
        }
        legacyLogger.writeLog(SEVERITY_ERROR, payload, defaultTag);
    }

    public LegacyCustomLogger getLegacyLogger() {
        return legacyLogger;
    }
}
