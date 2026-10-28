package com.systemdesign.lld.patterns.prototype.example03.bad;

import java.util.List;

/**
 * Flawed design: Shallow copies stage list and tasks.
 */
public class BadPipeline {
    public String name;
    public List<BadStage> stages;

    public BadPipeline(String name, List<BadStage> stages) {
        this.name = name;
        this.stages = stages;
    }

    // Flawed: Shallow copy
    public BadPipeline shallowCopy() {
        return new BadPipeline(this.name, this.stages);
    }
}
