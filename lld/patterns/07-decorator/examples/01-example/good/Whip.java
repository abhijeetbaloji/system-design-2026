package good;

/**
 * Concrete Decorator adding whipped cream.
 */
public class Whip extends CondimentDecorator {
    private static final double WHIP_COST = 0.40;

    public Whip(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Whip";
    }

    @Override
    public double getCost() {
        return beverage.getCost() + WHIP_COST;
    }
}
