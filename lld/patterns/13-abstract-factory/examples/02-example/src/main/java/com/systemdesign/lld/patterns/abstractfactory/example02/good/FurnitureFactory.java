package com.systemdesign.lld.patterns.abstractfactory.example02.good;

/**
 * Abstract Factory interface declaring creation methods for furniture families.
 */
public interface FurnitureFactory {
    Chair createChair();

    Sofa createSofa();

    CoffeeTable createCoffeeTable();

    String getStyleName();
}
