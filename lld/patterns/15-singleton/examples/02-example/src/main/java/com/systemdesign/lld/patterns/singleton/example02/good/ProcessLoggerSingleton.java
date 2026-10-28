package com.systemdesign.lld.patterns.singleton.example02.good;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe process-wide logger implemented via Double-Checked Locking (DCL) with volatile.
 */
public class ProcessLoggerSingleton implements LoggerService, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static volatile ProcessLoggerSingleton instance;
    private static volatile boolean instantiated = false;

    private volatile LogLevel currentLevel = LogLevel.INFO;
    private final List<LogRecord> logBuffer = new CopyOnWriteArrayList<>();

    // Private constructor with reflection defense
    private ProcessLoggerSingleton() {
        if (instantiated) {
            throw new IllegalStateException("ProcessLoggerSingleton already instantiated! Reflection blocked.");
        }
        instantiated = true;
    }

    public static ProcessLoggerSingleton getInstance() {
        ProcessLoggerSingleton localRef = instance;
        if (localRef == null) {
            synchronized (ProcessLoggerSingleton.class) {
                localRef = instance;
                if (localRef == null) {
                    instance = localRef = new ProcessLoggerSingleton();
                }
            }
        }
        return localRef;
    }

    @Override
    public void debug(String message) {
        appendLog(LogLevel.DEBUG, message);
    }

    @Override
    public void info(String message) {
        appendLog(LogLevel.INFO, message);
    }

    @Override
    public void warn(String message) {
        appendLog(LogLevel.WARN, message);
    }

    @Override
    public void error(String message) {
        appendLog(LogLevel.ERROR, message);
    }

    private void appendLog(LogLevel level, String message) {
        Objects.requireNonNull(message, "Log message cannot be null");
        if (level.isLoggable(currentLevel)) {
            LogRecord record = new LogRecord(level, message, System.currentTimeMillis(), Thread.currentThread().getName());
            logBuffer.add(record);
            System.out.println(record.format());
        }
    }

    @Override
    public List<LogRecord> getLogs() {
        return Collections.unmodifiableList(logBuffer);
    }

    @Override
    public void setLogLevel(LogLevel level) {
        this.currentLevel = Objects.requireNonNull(level, "LogLevel cannot be null");
    }

    @Override
    public LogLevel getLogLevel() {
        return currentLevel;
    }

    @Override
    public void clearLogs() {
        logBuffer.clear();
    }

    @Serial
    protected Object readResolve() {
        return getInstance();
    }
}
