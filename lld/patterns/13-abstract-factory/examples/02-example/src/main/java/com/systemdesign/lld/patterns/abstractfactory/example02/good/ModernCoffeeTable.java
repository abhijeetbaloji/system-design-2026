package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ModernCoffeeTable implements CoffeeTable {

    @Override
    public String placeItem(String item) {
        return "Placed '" + item + "' onto a frosted tempered-glass Modern coffee table.";
    }

    @Override
    public String getStyle() {
        return "Modern";
    }
}
