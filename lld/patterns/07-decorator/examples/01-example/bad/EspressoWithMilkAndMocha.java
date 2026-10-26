package bad;

/**
 * BAD DESIGN: Subclass explosion anti-pattern.
 *
 * Developers try to avoid runtime boolean flag configuration by creating specialized
 * subclasses for popular combinations.
 *
 * Consequences:
 * 1. For N drinks and M condiments, combinatorial explosion leads to hundreds of classes.
 * 2. Static coupling: Cannot add caramel or remove milk dynamically without making another class!
 * 3. Duplication of condiment pricing across multiple hardcoded subclasses.
 */
public class EspressoWithMilkAndMocha extends Espresso {
    public EspressoWithMilkAndMocha() {
        super();
        setMilk(true);
        addMocha();
    }

    @Override
    public double cost() {
        // Hardcoded combination pricing or redundant calculation
        return 1.99 + 0.50 + 0.75;
    }

    @Override
    public String getDescription() {
        return "Espresso with Milk and Mocha";
    }
}
