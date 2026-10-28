package com.systemdesign.lld.patterns.singleton.example02.bad;

import java.util.ArrayList;
import java.util.List;

public class BadFileLogger {
    private final String componentName;
    private final List<String> localBuffer = new ArrayList<>();

    // Flawed: Public constructor allows each module to create isolated loggers
    public BadFileLogger(String componentName) {
        this.componentName = componentName;
        System.out.println("[FILE OPEN] Opened independent file handle for logger: " + componentName);
    }

    public void log(String level, String message) {
        String entry = "[" + level + "] [" + componentName + "] " + message;
        localBuffer.add(entry);
        System.out.println(entry);
    }

    public List<String> getLocalBuffer() {
        return localBuffer;
    }
}
