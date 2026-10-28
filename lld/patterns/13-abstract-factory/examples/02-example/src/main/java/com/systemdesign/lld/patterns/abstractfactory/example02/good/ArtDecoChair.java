package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ArtDecoChair implements Chair {

    @Override
    public String sitOn() {
        return "Sitting on a glamorous Art Deco tub chair featuring fan-back velvet and golden brass legs.";
    }

    @Override
    public boolean hasLegs() {
        return true;
    }

    @Override
    public String getStyle() {
        return "Art Deco";
    }
}
