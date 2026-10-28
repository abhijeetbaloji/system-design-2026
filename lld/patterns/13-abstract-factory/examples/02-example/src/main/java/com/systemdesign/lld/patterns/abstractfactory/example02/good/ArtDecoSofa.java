package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ArtDecoSofa implements Sofa {

    @Override
    public String layOn() {
        return "Stretching out on a bold emerald-green Art Deco sofa with scalloped shell fluting.";
    }

    @Override
    public int getCapacity() {
        return 3;
    }

    @Override
    public String getStyle() {
        return "Art Deco";
    }
}
