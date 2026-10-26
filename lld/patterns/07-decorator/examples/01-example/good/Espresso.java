package good;

/**
 * Concrete Component representing a baseline Espresso shot.
 * Holds no references to condiments and has no conditional logic.
 */
public class Espresso implements Beverage {

    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double getCost() {
        return 1.99;
    }
}
