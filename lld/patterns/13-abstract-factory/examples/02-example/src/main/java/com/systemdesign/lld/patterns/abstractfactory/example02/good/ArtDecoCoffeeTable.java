package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class ArtDecoCoffeeTable implements CoffeeTable {

    @Override
    public String placeItem(String item) {
        return "Placed '" + item + "' onto a polished black marble and tiered gold Art Deco coffee table.";
    }

    @Override
    public String getStyle() {
        return "Art Deco";
    }
}
