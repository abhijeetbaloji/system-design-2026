package com.systemdesign.lld.patterns.abstractfactory.example02.bad;

public class BadFurnitureStore {
    private Object chair;
    private Object sofa;
    private Object coffeeTable;

    public void orderLivingRoomSet(String style) {
        if ("MODERN".equalsIgnoreCase(style)) {
            this.chair = new ModernChair();
            this.sofa = new ModernSofa();
            // BUG: Warehouse accidentally mixed in a Victorian coffee table!
            this.coffeeTable = new VictorianCoffeeTable();
        } else if ("VICTORIAN".equalsIgnoreCase(style)) {
            this.chair = new VictorianChair();
            this.sofa = new VictorianSofa();
            this.coffeeTable = new VictorianCoffeeTable();
        } else {
            throw new IllegalArgumentException("Unknown style: " + style);
        }
    }

    public Object getChair() {
        return chair;
    }

    public Object getSofa() {
        return sofa;
    }

    public Object getCoffeeTable() {
        return coffeeTable;
    }
}
