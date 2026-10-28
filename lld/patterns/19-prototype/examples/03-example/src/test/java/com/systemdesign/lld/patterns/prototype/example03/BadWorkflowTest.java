package com.systemdesign.lld.patterns.prototype.example03;

import com.systemdesign.lld.patterns.prototype.example03.bad.BadPipeline;
import com.systemdesign.lld.patterns.prototype.example03.bad.BadStage;
import com.systemdesign.lld.patterns.prototype.example03.bad.BadTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Bad Workflow Shallow Copy Leak Tests")
class BadWorkflowTest {

    @Test
    @DisplayName("Shallow copy shares stage list and env map corrupting baseline pipeline")
    void testShallowCopyLeak() {
        BadStage buildStage = new BadStage("Build");
        BadTask task = new BadTask("compile");
        task.env.put("ENV", "staging");
        buildStage.tasks.add(task);

        List<BadStage> stages = new ArrayList<>();
        stages.add(buildStage);

        BadPipeline original = new BadPipeline("Template", stages);
        BadPipeline clone = original.shallowCopy();

        assertSame(original.stages, clone.stages);

        // Mutating env on clone corrupts original template!
        clone.stages.get(0).tasks.get(0).env.put("ENV", "production");
        assertEquals("production", original.stages.get(0).tasks.get(0).env.get("ENV"),
                "Original pipeline env was corrupted by clone!");
    }
}
