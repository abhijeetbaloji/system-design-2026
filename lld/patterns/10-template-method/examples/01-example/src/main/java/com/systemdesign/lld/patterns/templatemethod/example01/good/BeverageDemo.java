package com.systemdesign.lld.patterns.templatemethod.example01.good;

public class BeverageDemo {
    public static void main(String[] args) {
        System.out.println("--- 1. Making Coffee with Condiments ---");
        CaffeineBeverage coffee = new Coffee(true);
        coffee.prepareRecipe();

        System.out.println("\n--- 2. Making Black Coffee (Hook override: no condiments) ---");
        CaffeineBeverage blackCoffee = new Coffee(false);
        blackCoffee.prepareRecipe();

        System.out.println("\n--- 3. Making Tea ---");
        CaffeineBeverage tea = new Tea();
        tea.prepareRecipe();
    }
}
