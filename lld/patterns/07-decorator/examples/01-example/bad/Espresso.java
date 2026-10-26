package bad;

public class Espresso extends Beverage {
    public Espresso() {
        super("Espresso");
    }

    @Override
    public double cost() {
        // Adds base espresso cost to whatever condiments are set on the superclass
        return 1.99 + super.cost();
    }
}
