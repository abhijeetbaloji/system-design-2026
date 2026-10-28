package com.systemdesign.lld.patterns.prototype.example03.good;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TaskStep component supporting deep cloning.
 * Deeply duplicates command strings, timeouts, and environment variables map.
 */
public class TaskStep implements WorkflowPrototype<TaskStep> {
    private final String name;
    private final String command;
    private final int timeoutSeconds;
    private final Map<String, String> envVars = new LinkedHashMap<>();

    public TaskStep(String name, String command, int timeoutSeconds) {
        this.name = Objects.requireNonNull(name, "Step name cannot be null");
        this.command = Objects.requireNonNull(command, "Command cannot be null");
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("Timeout must be positive");
        }
        this.timeoutSeconds = timeoutSeconds;
    }

    public TaskStep(TaskStep source) {
        Objects.requireNonNull(source, "Source step cannot be null");
        this.name = source.name;
        this.command = source.command;
        this.timeoutSeconds = source.timeoutSeconds;
        // Deep copy of environment variables map
        this.envVars.putAll(source.envVars);
    }

    @Override
    public TaskStep clone() {
        return new TaskStep(this);
    }

    public void setEnvVar(String key, String value) {
        envVars.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public Map<String, String> getEnvVars() {
        return Collections.unmodifiableMap(envVars);
    }

    @Override
    public String toString() {
        return String.format("Task['%s', cmd='%s', timeout=%ds, env=%s]",
                name, command, timeoutSeconds, envVars);
    }
}
