package com.systemdesign.lld.patterns.abstractfactory.example02.good;

import java.util.Objects;

/**
 * Client class representing a coordinated living room suite assembled from a furniture factory.
 */
public class LivingRoomSuite {
    private final Chair chair;
    private final Sofa sofa;
    private final CoffeeTable coffeeTable;
    private final String suiteStyle;

    public LivingRoomSuite(FurnitureFactory factory) {
        Objects.requireNonNull(factory, "FurnitureFactory cannot be null");
        this.chair = factory.createChair();
        this.sofa = factory.createSofa();
        this.coffeeTable = factory.createCoffeeTable();
        this.suiteStyle = factory.getStyleName();
    }

    public boolean isHarmonious() {
        return chair.getStyle().equalsIgnoreCase(suiteStyle)
                && sofa.getStyle().equalsIgnoreCase(suiteStyle)
                && coffeeTable.getStyle().equalsIgnoreCase(suiteStyle);
    }

    public String describeSuite() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(" LIVING ROOM SUITE (Style: ").append(suiteStyle).append(")\n");
        sb.append(" Visual Harmony Check: ").append(isHarmonious() ? "PERFECT MATCH" : "STYLE CLASH!").append("\n");
        sb.append("====================================================\n");
        sb.append(" - Chair:        ").append(chair.sitOn()).append("\n");
        sb.append(" - Sofa:         ").append(sofa.layOn()).append(" [Capacity: ").append(sofa.getCapacity()).append("]\n");
        sb.append(" - Coffee Table: ").append(coffeeTable.placeItem("Design Magazine")).append("\n");
        sb.append("====================================================\n");
        return sb.toString();
    }

    public Chair getChair() {
        return chair;
    }

    public Sofa getSofa() {
        return sofa;
    }

    public CoffeeTable getCoffeeTable() {
        return coffeeTable;
    }

    public String getSuiteStyle() {
        return suiteStyle;
    }
}
