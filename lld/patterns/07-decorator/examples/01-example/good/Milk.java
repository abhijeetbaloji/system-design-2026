package good;

/**
 * Concrete Decorator adding steamed milk.
 *
 * Responsibilities:
 * - Appends ", Milk" to the wrapped beverage's description.
 * - Adds $0.50 to the wrapped beverage's cost.
 */
public class Milk extends CondimentDecorator {
    private static final double MILK_COST = 0.50;

    public Milk(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return beverage.getCost() + MILK_COST;
    }
}
