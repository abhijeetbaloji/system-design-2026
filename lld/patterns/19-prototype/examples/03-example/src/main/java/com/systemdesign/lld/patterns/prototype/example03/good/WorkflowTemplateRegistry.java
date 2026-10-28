package com.systemdesign.lld.patterns.prototype.example03.good;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry holding pre-configured CI/CD workflow templates.
 * Returns completely isolated, deep-cloned workflows ready for specialization.
 */
public class WorkflowTemplateRegistry {
    private final Map<String, PipelineWorkflow> templates = new ConcurrentHashMap<>();

    public void registerTemplate(String key, PipelineWorkflow template) {
        Objects.requireNonNull(key, "Template key cannot be null");
        Objects.requireNonNull(template, "Template cannot be null");
        templates.put(key, template);
    }

    public PipelineWorkflow clonePipeline(String key) {
        PipelineWorkflow template = templates.get(key);
        if (template == null) {
            throw new NoSuchElementException("No workflow template registered for key: " + key);
        }
        return template.clone(); // Crucial: Always clone before returning!
    }

    public boolean hasTemplate(String key) {
        return templates.containsKey(key);
    }

    public int getTemplateCount() {
        return templates.size();
    }
}
