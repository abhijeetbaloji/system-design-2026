package com.systemdesign.lld.patterns.templatemethod.example01.good;

public class Coffee extends CaffeineBeverage {

    private final boolean wantsCondiments;

    public Coffee(boolean wantsCondiments) {
        this.wantsCondiments = wantsCondiments;
    }

    public Coffee() {
        this(true);
    }

    @Override
    protected void brew() {
        recordStep("Dripping artisanal coffee grounds through filter");
        System.out.println("[Coffee] Dripping artisanal coffee grounds through filter");
    }

    @Override
    protected void addCondiments() {
        recordStep("Adding sugar and oat milk");
        System.out.println("[Coffee] Adding sugar and oat milk");
    }

    @Override
    protected boolean customerWantsCondiments() {
        return this.wantsCondiments;
    }
}
