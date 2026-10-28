package com.systemdesign.lld.patterns.abstractfactory.example02.good;

public class FurnitureStoreDemo {
    public static void main(String[] args) {
        FurnitureFactory modernFactory = new ModernFurnitureFactory();
        LivingRoomSuite modernSuite = new LivingRoomSuite(modernFactory);
        System.out.println(modernSuite.describeSuite());

        FurnitureFactory victorianFactory = new VictorianFurnitureFactory();
        LivingRoomSuite victorianSuite = new LivingRoomSuite(victorianFactory);
        System.out.println(victorianSuite.describeSuite());

        FurnitureFactory artDecoFactory = new ArtDecoFurnitureFactory();
        LivingRoomSuite artDecoSuite = new LivingRoomSuite(artDecoFactory);
        System.out.println(artDecoSuite.describeSuite());
    }
}
