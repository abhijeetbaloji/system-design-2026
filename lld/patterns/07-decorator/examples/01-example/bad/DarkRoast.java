package bad;

public class DarkRoast extends Beverage {
    public DarkRoast() {
        super("Dark Roast Coffee");
    }

    @Override
    public double cost() {
        return 2.49 + super.cost();
    }
}
