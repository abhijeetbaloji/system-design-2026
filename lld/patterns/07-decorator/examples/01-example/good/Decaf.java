package good;

/**
 * Concrete Component representing a Decaf coffee.
 */
public class Decaf implements Beverage {

    @Override
    public String getDescription() {
        return "Decaf Coffee";
    }

    @Override
    public double getCost() {
        return 2.19;
    }
}
