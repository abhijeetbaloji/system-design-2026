package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class VictorianChair implements Chair {

    @Override
    public String sitOn() {
        return "Sitting on a high-back Victorian armchair with carved mahogany armrests and red velvet.";
    }

    @Override
    public boolean hasLegs() {
        return true; // 4 cabriole legs
    }

    @Override
    public String getStyle() {
        return "Victorian";
    }
}
