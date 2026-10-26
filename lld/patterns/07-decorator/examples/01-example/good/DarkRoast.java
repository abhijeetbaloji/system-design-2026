package good;

/**
 * Concrete Component representing a Dark Roast coffee.
 */
public class DarkRoast implements Beverage {

    @Override
    public String getDescription() {
        return "Dark Roast Coffee";
    }

    @Override
    public double getCost() {
        return 2.49;
    }
}
