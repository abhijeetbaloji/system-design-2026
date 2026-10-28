package com.systemdesign.lld.patterns.abstractfactory.example02.bad;

public class BadFurnitureDemo {
    public static void main(String[] args) {
        BadFurnitureStore store = new BadFurnitureStore();
        store.orderLivingRoomSet("MODERN");

        System.out.println("Customer received mixed suite:");
        System.out.println("Chair: " + ((ModernChair) store.getChair()).sitOn());
        System.out.println("Sofa: " + ((ModernSofa) store.getSofa()).layOn());
        System.out.println("Coffee Table: " + ((VictorianCoffeeTable) store.getCoffeeTable()).placeItem("Espresso"));
    }
}
