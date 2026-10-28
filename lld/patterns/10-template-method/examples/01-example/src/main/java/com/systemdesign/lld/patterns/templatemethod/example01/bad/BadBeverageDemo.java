package com.systemdesign.lld.patterns.templatemethod.example01.bad;

public class BadBeverageDemo {
    public static void main(String[] args) {
        System.out.println("--- Making Bad Coffee ---");
        BadCoffee coffee = new BadCoffee();
        coffee.prepareRecipe();

        System.out.println("\n--- Making Bad Tea ---");
        BadTea tea = new BadTea();
        tea.prepareRecipe();
    }
}
