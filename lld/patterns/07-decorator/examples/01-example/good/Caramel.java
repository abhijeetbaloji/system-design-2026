package good;

/**
 * Concrete Decorator adding rich caramel drizzle.
 */
public class Caramel extends CondimentDecorator {
    private static final double CARAMEL_COST = 0.80;

    public Caramel(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Caramel";
    }

    @Override
    public double getCost() {
        return beverage.getCost() + CARAMEL_COST;
    }
}
