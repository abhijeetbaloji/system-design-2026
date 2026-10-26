package good;

/**
 * Concrete Decorator adding soy milk.
 */
public class Soy extends CondimentDecorator {
    private static final double SOY_COST = 0.60;

    public Soy(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }

    @Override
    public double getCost() {
        return beverage.getCost() + SOY_COST;
    }
}
