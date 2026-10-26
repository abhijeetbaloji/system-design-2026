package good;

/**
 * Concrete Decorator adding chocolate mocha syrup.
 *
 * Demonstrates composability: Because Mocha wraps any Beverage,
 * a customer can order double or triple mocha simply by nesting Mocha decorators:
 * new Mocha(new Mocha(beverage))
 */
public class Mocha extends CondimentDecorator {
    private static final double MOCHA_COST = 0.75;

    public Mocha(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Mocha";
    }

    @Override
    public double getCost() {
        return beverage.getCost() + MOCHA_COST;
    }
}
