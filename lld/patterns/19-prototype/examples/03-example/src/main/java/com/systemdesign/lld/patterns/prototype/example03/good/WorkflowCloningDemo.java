package com.systemdesign.lld.patterns.prototype.example03.good;

public class WorkflowCloningDemo {
    public static void main(String[] args) {
        WorkflowTemplateRegistry registry = new WorkflowTemplateRegistry();

        // 1. Construct baseline Golden Pipeline
        PipelineWorkflow mavenTemplate = new PipelineWorkflow("Standard-Java-Service", "GIT_PUSH_MAIN");

        // Build Stage
        PipelineStage buildStage = new PipelineStage("Build & Compile", false);
        TaskStep mvnCompile = new TaskStep("mvn-compile", "mvn clean compile", 300);
        mvnCompile.setEnvVar("JAVA_VERSION", "21");
        mvnCompile.setEnvVar("ENVIRONMENT", "baseline");
        buildStage.addStep(mvnCompile);

        // Test Stage
        PipelineStage testStage = new PipelineStage("Automated Testing", true);
        TaskStep unitTest = new TaskStep("unit-tests", "mvn test", 600);
        unitTest.setEnvVar("ENVIRONMENT", "baseline");
        testStage.addStep(unitTest);

        mavenTemplate.addStage(buildStage);
        mavenTemplate.addStage(testStage);

        registry.registerTemplate("maven-baseline", mavenTemplate);

        PipelineExecutionService service = new PipelineExecutionService(registry);

        // 2. Clone and specialize for Order Microservice (Staging)
        PipelineWorkflow orderPipeline = service.setupServicePipeline("maven-baseline", "OrderService", "staging");

        // 3. Clone and specialize for Payment Microservice (Production) + add compliance step
        PipelineWorkflow paymentPipeline = service.setupServicePipeline("maven-baseline", "PaymentService", "production");
        PipelineStage complianceStage = new PipelineStage("Compliance & Audit", false);
        TaskStep auditScan = new TaskStep("pci-scan", "run-pci-audit --strict", 900);
        auditScan.setEnvVar("AUDITOR", "SecurityHQ");
        complianceStage.addStep(auditScan);
        paymentPipeline.addStage(complianceStage);

        System.out.println("=== ORDER SERVICE PIPELINE (Staging) ===");
        System.out.println(orderPipeline);

        System.out.println("=== PAYMENT SERVICE PIPELINE (Production with Compliance) ===");
        System.out.println(paymentPipeline);

        System.out.println("=== BASELINE TEMPLATE (Unaltered) ===");
        System.out.println(mavenTemplate);
    }
}
