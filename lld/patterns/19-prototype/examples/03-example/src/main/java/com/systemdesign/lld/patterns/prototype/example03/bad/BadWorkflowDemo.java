package com.systemdesign.lld.patterns.prototype.example03.bad;

import java.util.ArrayList;
import java.util.List;

public class BadWorkflowDemo {
    public static void main(String[] args) {
        BadStage buildStage = new BadStage("Build");
        BadTask compileTask = new BadTask("mvn compile");
        compileTask.env.put("ENV", "staging");
        buildStage.tasks.add(compileTask);

        List<BadStage> stages = new ArrayList<>();
        stages.add(buildStage);

        BadPipeline template = new BadPipeline("Golden Pipeline", stages);

        // Production pipeline clones template
        BadPipeline prodPipeline = template.shallowCopy();
        prodPipeline.name = "Production Pipeline";

        // Override env on clone
        prodPipeline.stages.get(0).tasks.get(0).env.put("ENV", "production");

        System.out.println("Prod ENV:     " + prodPipeline.stages.get(0).tasks.get(0).env.get("ENV"));
        System.out.println("Template ENV: " + template.stages.get(0).tasks.get(0).env.get("ENV")); // BUG: production!
    }
}
