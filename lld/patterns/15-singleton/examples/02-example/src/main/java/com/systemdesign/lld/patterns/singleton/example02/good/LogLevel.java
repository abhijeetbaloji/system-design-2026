package com.systemdesign.lld.patterns.singleton.example02.good;

public enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private final int severity;

    LogLevel(int severity) {
        this.severity = severity;
    }

    public int getSeverity() {
        return severity;
    }

    public boolean isLoggable(LogLevel currentThreshold) {
        return this.severity >= currentThreshold.severity;
    }
}
