package com.systemdesign.lld.patterns.prototype.example03.good;

import java.util.Objects;

/**
 * Service demonstrating pipeline cloning, specialization, and execution.
 */
public class PipelineExecutionService {
    private final WorkflowTemplateRegistry registry;

    public PipelineExecutionService(WorkflowTemplateRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "Registry cannot be null");
    }

    public PipelineWorkflow setupServicePipeline(String templateKey, String serviceName, String targetEnv) {
        PipelineWorkflow pipeline = registry.clonePipeline(templateKey);
        pipeline.setPipelineName(serviceName + "-Pipeline");
        pipeline.overrideAllEnvVars("ENVIRONMENT", targetEnv);
        pipeline.overrideAllEnvVars("SERVICE_NAME", serviceName);
        return pipeline;
    }
}
