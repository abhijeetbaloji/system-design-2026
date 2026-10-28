package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class VictorianSofa implements Sofa {

    @Override
    public String layOn() {
        return "Resting on a tufted Victorian Chesterfield sofa with ornate brass studs.";
    }

    @Override
    public int getCapacity() {
        return 3;
    }

    @Override
    public String getStyle() {
        return "Victorian";
    }
}
