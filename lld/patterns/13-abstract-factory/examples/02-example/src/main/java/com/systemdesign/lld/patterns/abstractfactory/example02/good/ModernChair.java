package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ModernChair implements Chair {

    @Override
    public String sitOn() {
        return "Sitting on a Modern ergonomic cantilever chair with black leather and brushed steel.";
    }

    @Override
    public boolean hasLegs() {
        return false; // Cantilever base
    }

    @Override
    public String getStyle() {
        return "Modern";
    }
}
