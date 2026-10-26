package bad;

public class BadCoffeeDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Coffee Design Demo ===");

        // Flaw 1: Boolean mutation in base class
        Beverage customBeverage = new DarkRoast();
        customBeverage.setMilk(true);
        customBeverage.addMocha();
        customBeverage.addMocha(); // double mocha
        customBeverage.setWhip(true);

        System.out.printf("Drink: %s%n", customBeverage.getDescription());
        System.out.printf("Total Cost: $%.2f%n", customBeverage.cost());

        // Flaw 2: Rigid Subclass attempt
        Beverage combo = new EspressoWithMilkAndMocha();
        System.out.printf("Combo Drink: %s%n", combo.getDescription());
        System.out.printf("Combo Cost: $%.2f%n", combo.cost());

        System.out.println("\nProblems exposed:");
        System.out.println("1. Cannot add OatMilk or Caramel without modifying base Beverage class.");
        System.out.println("2. Subclasses like EspressoWithMilkAndMocha cannot dynamically support an extra shot of whip.");
        System.out.println("3. Combinations scale exponentially (2^N), making subclassing unmaintainable.");
    }
}
