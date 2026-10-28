package com.systemdesign.lld.patterns.prototype.example03;

import com.systemdesign.lld.patterns.prototype.example03.good.PipelineExecutionService;
import com.systemdesign.lld.patterns.prototype.example03.good.PipelineStage;
import com.systemdesign.lld.patterns.prototype.example03.good.PipelineWorkflow;
import com.systemdesign.lld.patterns.prototype.example03.good.TaskStep;
import com.systemdesign.lld.patterns.prototype.example03.good.WorkflowTemplateRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Workflow Prototype Pattern Tests")
class WorkflowPrototypeTest {

    private WorkflowTemplateRegistry registry;
    private PipelineWorkflow goldenTemplate;

    @BeforeEach
    void setUp() {
        registry = new WorkflowTemplateRegistry();

        goldenTemplate = new PipelineWorkflow("Base-Pipeline", "PUSH_MAIN");
        PipelineStage buildStage = new PipelineStage("Build", false);
        TaskStep compileStep = new TaskStep("compile", "mvn compile", 180);
        compileStep.setEnvVar("ENVIRONMENT", "base");
        buildStage.addStep(compileStep);

        goldenTemplate.addStage(buildStage);
        registry.registerTemplate("base-pipeline", goldenTemplate);
    }

    @Test
    @DisplayName("Registry returns a distinct cloned workflow instance rather than the cached reference")
    void testRegistryCloning() {
        PipelineWorkflow clone1 = registry.clonePipeline("base-pipeline");
        PipelineWorkflow clone2 = registry.clonePipeline("base-pipeline");

        assertNotSame(goldenTemplate, clone1);
        assertNotSame(clone1, clone2);
        assertEquals(goldenTemplate.getPipelineName(), clone1.getPipelineName());
    }

    @Test
    @DisplayName("Adding a stage to clone does not mutate golden template")
    void testStageIsolation() {
        PipelineWorkflow clone = registry.clonePipeline("base-pipeline");
        PipelineStage deployStage = new PipelineStage("Deploy", false);
        deployStage.addStep(new TaskStep("deploy-k8s", "kubectl apply", 300));
        clone.addStage(deployStage);

        assertEquals(2, clone.getStages().size());
        assertEquals(1, goldenTemplate.getStages().size(), "Golden template must remain with 1 stage!");
    }

    @Test
    @DisplayName("Overriding environment variables in clone does not corrupt golden template")
    void testEnvVarIsolation() {
        PipelineWorkflow clone = registry.clonePipeline("base-pipeline");
        clone.overrideAllEnvVars("ENVIRONMENT", "production");

        TaskStep cloneStep = clone.getStages().get(0).getSteps().get(0);
        TaskStep templateStep = goldenTemplate.getStages().get(0).getSteps().get(0);

        assertEquals("production", cloneStep.getEnvVars().get("ENVIRONMENT"));
        assertEquals("base", templateStep.getEnvVars().get("ENVIRONMENT"), "Template env var must not change!");
    }

    @Test
    @DisplayName("PipelineExecutionService configures microservice pipelines cleanly")
    void testPipelineExecutionService() {
        PipelineExecutionService service = new PipelineExecutionService(registry);
        PipelineWorkflow orderPipeline = service.setupServicePipeline("base-pipeline", "OrderService", "staging");

        assertEquals("OrderService-Pipeline", orderPipeline.getPipelineName());
        TaskStep step = orderPipeline.getStages().get(0).getSteps().get(0);
        assertEquals("staging", step.getEnvVars().get("ENVIRONMENT"));
        assertEquals("OrderService", step.getEnvVars().get("SERVICE_NAME"));
    }

    @Test
    @DisplayName("Registry throws NoSuchElementException for unknown template key")
    void testUnknownTemplateThrows() {
        assertThrows(NoSuchElementException.class, () -> registry.clonePipeline("non-existent"));
    }
}
