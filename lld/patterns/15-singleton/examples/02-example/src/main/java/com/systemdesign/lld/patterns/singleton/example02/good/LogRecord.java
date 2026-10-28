package com.systemdesign.lld.patterns.singleton.example02.good;

import java.util.Objects;

public class LogRecord {
    private final LogLevel level;
    private final String message;
    private final long timestamp;
    private final String threadName;

    public LogRecord(LogLevel level, String message, long timestamp, String threadName) {
        this.level = Objects.requireNonNull(level, "Level cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.timestamp = timestamp;
        this.threadName = Objects.requireNonNull(threadName, "Thread name cannot be null");
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public String format() {
        return String.format("[%tF %<tT.%<tL] [%s] [%s] %s", timestamp, threadName, level, message);
    }

    @Override
    public String toString() {
        return format();
    }
}
