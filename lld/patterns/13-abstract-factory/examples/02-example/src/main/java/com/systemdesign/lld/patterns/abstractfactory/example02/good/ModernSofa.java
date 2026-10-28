package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ModernSofa implements Sofa {

    @Override
    public String layOn() {
        return "Lounging on a low-profile Modern modular sectional with clean geometric lines.";
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    @Override
    public String getStyle() {
        return "Modern";
    }
}
