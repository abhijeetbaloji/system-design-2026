package com.systemdesign.lld.patterns.adapter.example03.good;

/**
 * Target Interface:
 * Modern logging abstraction conforming to standard enterprise logging frameworks.
 */
public interface AppLogger {

    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    void error(String message, Throwable throwable);
}
