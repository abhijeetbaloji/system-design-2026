package com.systemdesign.lld.patterns.templatemethod.example01.good;

public class Tea extends CaffeineBeverage {

    @Override
    protected void brew() {
        recordStep("Steeping organic Earl Grey tea leaves for 4 minutes");
        System.out.println("[Tea] Steeping organic Earl Grey tea leaves for 4 minutes");
    }

    @Override
    protected void addCondiments() {
        recordStep("Adding slice of fresh organic lemon");
        System.out.println("[Tea] Adding slice of fresh organic lemon");
    }
}
