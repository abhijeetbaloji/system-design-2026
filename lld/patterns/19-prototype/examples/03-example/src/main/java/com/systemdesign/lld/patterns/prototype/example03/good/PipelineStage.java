package com.systemdesign.lld.patterns.prototype.example03.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * PipelineStage component holding a group of task steps.
 * Deeply duplicates the task steps list by cloning every individual step.
 */
public class PipelineStage implements WorkflowPrototype<PipelineStage> {
    private final String stageName;
    private final boolean parallel;
    private final List<TaskStep> steps = new ArrayList<>();

    public PipelineStage(String stageName, boolean parallel) {
        this.stageName = Objects.requireNonNull(stageName, "Stage name cannot be null");
        this.parallel = parallel;
    }

    public PipelineStage(PipelineStage source) {
        Objects.requireNonNull(source, "Source stage cannot be null");
        this.stageName = source.stageName;
        this.parallel = source.parallel;

        // Strict Deep Copy of all task steps
        for (TaskStep step : source.steps) {
            this.steps.add(step.clone());
        }
    }

    @Override
    public PipelineStage clone() {
        return new PipelineStage(this);
    }

    public void addStep(TaskStep step) {
        steps.add(Objects.requireNonNull(step, "Step cannot be null"));
    }

    public String getStageName() {
        return stageName;
    }

    public boolean isParallel() {
        return parallel;
    }

    public List<TaskStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    @Override
    public String toString() {
        return String.format("Stage['%s', parallel=%s, stepsCount=%d]",
                stageName, parallel, steps.size());
    }
}
