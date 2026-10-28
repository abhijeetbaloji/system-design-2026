# Example 03: Configurable CI/CD Pipeline Template Cloning

## 1. Problem Scenario
An enterprise DevOps engineering platform manages thousands of microservice deployment pipelines across development, staging, and production clusters.
Constructing a CI/CD pipeline from scratch involves:
- Defining stages (`Build`, `Test`, `Security Scan`, `Deploy`).
- Defining task steps with bash commands, timeouts, retry limits, and environment variable maps.
- Setting container orchestration flags (e.g., parallel test sharding).

Without the Prototype pattern:
- Teams copy pipelines manually or write boilerplate constructors that perform shallow copies.
- Overriding an environment variable (`ENVIRONMENT=production`) or adding a custom security step on a cloned pipeline corrupts the shared staging template or pollutes other microservices.

## 2. Bad Design (Shared Mutable State)
- In the bad design, `BadPipeline` copies references to `List<BadStage>`.
- Adding a custom notification step or mutating environment variables on the clone mutates the shared stage list inside the baseline template.
- Development builds accidentally execute against production credentials due to shared environment maps.

## 3. Good Design (Prototype Pattern)
- `WorkflowPrototype<T>`: Generic type-safe cloning contract.
- `TaskStep`: Deep-clones command strings, timeouts, and creates a fresh `LinkedHashMap` for environment variables.
- `PipelineStage`: Deep-clones its `List<TaskStep>` by invoking `.clone()` on every step.
- `PipelineWorkflow`: Top-level aggregate deep-cloning its `List<PipelineStage>` by calling `.clone()` on every stage.
- `WorkflowTemplateRegistry`: Stores validated golden baseline pipeline definitions (e.g., `"standard-maven-pipeline"`).
  - Calling `registry.clonePipeline("standard-maven-pipeline")` returns an entirely independent, multi-tiered clone.
  - The caller can customize steps, override environment variables, and append stages safely without touching the golden template.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/19-prototype/examples/03-example/diagram.mmd).

## 5. Execution Flow
1. Platform engineer registers `"standard-maven-pipeline"` containing `Build`, `Test`, and `Deploy` stages.
2. Order Service clones the pipeline template: `registry.clonePipeline("standard-maven-pipeline")`.
3. Order Service overrides `ENVIRONMENT=staging` and adds an integration test step.
4. Payment Service clones the same pipeline template: `registry.clonePipeline("standard-maven-pipeline")`.
5. Payment Service overrides `ENVIRONMENT=production` and adds a PCI-DSS compliance scan step.
6. Both pipelines execute with complete isolation, leaving the baseline template intact.
