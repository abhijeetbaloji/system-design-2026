package com.systemdesign.lld.patterns.prototype.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Top-level Workflow Prototype representing a full CI/CD deployment pipeline.
 * Performs deep recursive cloning of stages, steps, and environment parameters.
 */
public class PipelineWorkflow implements WorkflowPrototype<PipelineWorkflow> {
    private String pipelineName;
    private final String triggerEvent;
    private final List<PipelineStage> stages = new ArrayList<>();

    public PipelineWorkflow(String pipelineName, String triggerEvent) {
        this.pipelineName = Objects.requireNonNull(pipelineName, "Pipeline name cannot be null");
        this.triggerEvent = Objects.requireNonNull(triggerEvent, "Trigger event cannot be null");
    }

    public PipelineWorkflow(PipelineWorkflow source) {
        Objects.requireNonNull(source, "Source workflow cannot be null");
        this.pipelineName = source.pipelineName;
        this.triggerEvent = source.triggerEvent;

        // Strict Deep Copy of all stages
        for (PipelineStage stage : source.stages) {
            this.stages.add(stage.clone());
        }
    }

    @Override
    public PipelineWorkflow clone() {
        return new PipelineWorkflow(this);
    }

    public void addStage(PipelineStage stage) {
        stages.add(Objects.requireNonNull(stage, "Stage cannot be null"));
    }

    public void overrideAllEnvVars(String key, String value) {
        for (PipelineStage stage : stages) {
            for (TaskStep step : stage.getSteps()) {
                step.setEnvVar(key, value);
            }
        }
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = Objects.requireNonNull(pipelineName);
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public List<PipelineStage> getStages() {
        return Collections.unmodifiableList(stages);
    }

    public String formatSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== PIPELINE: %s (Trigger: %s) ===\n", pipelineName, triggerEvent));
        for (PipelineStage stage : stages) {
            sb.append(String.format(" [Stage: %s, Parallel: %s]\n", stage.getStageName(), stage.isParallel()));
            for (TaskStep step : stage.getSteps()) {
                sb.append("   - ").append(step).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatSummary();
    }
}
