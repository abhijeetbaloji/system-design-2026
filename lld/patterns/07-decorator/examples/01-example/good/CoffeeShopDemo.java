package good;

public class CoffeeShopDemo {
    public static void main(String[] args) {
        System.out.println("=== Coffee Shop - Decorator Pattern Demo ===");

        // 1. Plain Espresso without condiments
        Beverage espresso = new Espresso();
        printBeverage(espresso);

        // 2. Dark Roast with Double Mocha and Whip
        // Shows recursive wrapping: Mocha wraps Mocha, which wraps DarkRoast, which is wrapped by Whip
        Beverage deluxeDarkRoast = new DarkRoast();
        deluxeDarkRoast = new Mocha(deluxeDarkRoast);
        deluxeDarkRoast = new Mocha(deluxeDarkRoast); // Double mocha!
        deluxeDarkRoast = new Whip(deluxeDarkRoast);
        printBeverage(deluxeDarkRoast);

        // 3. Fully customized Decaf order composed in a single fluent expression
        Beverage caramelSoyDecaf = new Caramel(
                                       new Soy(
                                           new Decaf()));
        printBeverage(caramelSoyDecaf);

        // 4. Dynamic runtime enhancement: Adding milk to the caramelSoyDecaf on the fly
        caramelSoyDecaf = new Milk(caramelSoyDecaf);
        System.out.println("-> Customer added Milk to their Decaf order at the counter:");
        printBeverage(caramelSoyDecaf);
    }

    private static void printBeverage(Beverage beverage) {
        System.out.printf("Order: %-45s | Price: $%.2f%n",
                beverage.getDescription(), beverage.getCost());
    }
}
