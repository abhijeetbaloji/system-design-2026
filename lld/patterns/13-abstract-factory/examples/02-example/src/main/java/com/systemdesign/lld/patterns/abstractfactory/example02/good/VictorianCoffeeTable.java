package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class VictorianCoffeeTable implements CoffeeTable {

    @Override
    public String placeItem(String item) {
        return "Placed '" + item + "' upon a heavy carved oak Victorian coffee table with brass inlays.";
    }

    @Override
    public String getStyle() {
        return "Victorian";
    }
}
