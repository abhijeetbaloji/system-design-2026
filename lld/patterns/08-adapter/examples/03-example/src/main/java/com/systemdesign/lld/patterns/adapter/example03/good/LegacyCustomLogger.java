package com.systemdesign.lld.patterns.adapter.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptee:
 * Legacy logging system from older internal framework.
 * Incompatible interface:
 * - writeLog(int severityCode, String payload, String tag)
 * - severityCode: 1 = DEBUG, 2 = INFO, 3 = WARN, 4 = ERROR
 */
public class LegacyCustomLogger {

    private final List<String> logRecords = new ArrayList<>();

    public void writeLog(int severityCode, String payload, String tag) {
        String level = switch (severityCode) {
            case 1 -> "DEBUG";
            case 2 -> "INFO";
            case 3 -> "WARN";
            case 4 -> "ERROR";
            default -> "UNKNOWN";
        };
        String formatted = String.format("[%s] [%s] %s", level, tag, payload);
        logRecords.add(formatted);
        System.out.println(formatted);
    }

    public List<String> getLogRecords() {
        return Collections.unmodifiableList(logRecords);
    }
}
