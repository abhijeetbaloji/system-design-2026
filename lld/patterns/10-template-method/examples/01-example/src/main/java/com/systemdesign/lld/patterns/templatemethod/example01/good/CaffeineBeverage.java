package com.systemdesign.lld.patterns.templatemethod.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract Base Class:
 * Defines the Template Method specifying the fixed algorithm skeleton for preparing hot beverages.
 */
public abstract class CaffeineBeverage {

    private final List<String> executionLog = new ArrayList<>();

    /**
     * The Template Method.
     * Declared final to prevent subclasses from altering or reordering the sequence of execution.
     */
    public final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        } else {
            recordStep("Condiments skipped by customer request");
        }
    }

    // Invariant step: Common to all beverages
    private void boilWater() {
        recordStep("Boiling water to 100°C");
        System.out.println("[CaffeineBeverage] Boiling water to 100°C");
    }

    // Primitive operation: Must be implemented by concrete subclasses
    protected abstract void brew();

    // Invariant step: Common to all beverages
    private void pourInCup() {
        recordStep("Pouring into ceramic cup");
        System.out.println("[CaffeineBeverage] Pouring into ceramic cup");
    }

    // Primitive operation: Must be implemented by concrete subclasses
    protected abstract void addCondiments();

    /**
     * Hook Method:
     * Provides a default implementation. Subclasses may optionally override
     * this method to control conditional execution without changing the template.
     */
    protected boolean customerWantsCondiments() {
        return true;
    }

    protected void recordStep(String step) {
        executionLog.add(step);
    }

    public List<String> getExecutionLog() {
        return Collections.unmodifiableList(executionLog);
    }
}
