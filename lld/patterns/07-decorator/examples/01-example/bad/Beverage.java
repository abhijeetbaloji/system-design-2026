package bad;

/**
 * BAD DESIGN: Monolithic Beverage class attempting to manage all possible condiments
 * using boolean flags, count fields, and conditional logic.
 *
 * Design Flaws:
 * 1. Violates Open/Closed Principle (OCP): Adding a new condiment (e.g., OatMilk)
 *    requires editing this base class and modifying cost() and getDescription().
 * 2. Violates Single Responsibility Principle (SRP): Beverage is responsible for
 *    both core beverage details AND pricing/rules for every condiment in the shop.
 * 3. Rigid & Fragile: Double/triple condiments require clunky counter fields.
 * 4. Inappropriate Inheritance: Subclasses like Tea or Water inherit milk/mocha options.
 */
public class Beverage {
    private String description = "Unknown Beverage";

    // Boolean flags and counters for condiments
    private boolean milk;
    private boolean soy;
    private boolean mocha;
    private boolean whip;
    private int mochaCount = 0; // Clunky attempt to support multiple shots of mocha

    // Condiment prices hardcoded directly into the base beverage
    private static final double MILK_PRICE = 0.50;
    private static final double SOY_PRICE = 0.60;
    private static final double MOCHA_PRICE = 0.75;
    private static final double WHIP_PRICE = 0.40;

    public Beverage(String description) {
        this.description = description;
    }

    public String getDescription() {
        StringBuilder fullDescription = new StringBuilder(description);
        if (milk) fullDescription.append(", Milk");
        if (soy) fullDescription.append(", Soy");
        if (mocha) {
            fullDescription.append(", Mocha");
            if (mochaCount > 1) {
                fullDescription.append(" x").append(mochaCount);
            }
        }
        if (whip) fullDescription.append(", Whip");
        return fullDescription.toString();
    }

    /**
     * Base cost calculation. Every time a condiment price changes or a new condiment
     * is added, this method must be rewritten, risking regression bugs.
     */
    public double cost() {
        double condimentCost = 0.0;
        if (milk) condimentCost += MILK_PRICE;
        if (soy) condimentCost += SOY_PRICE;
        if (mocha) {
            condimentCost += (mochaCount > 0 ? mochaCount : 1) * MOCHA_PRICE;
        }
        if (whip) condimentCost += WHIP_PRICE;
        return condimentCost;
    }

    public void setMilk(boolean milk) { this.milk = milk; }
    public void setSoy(boolean soy) { this.soy = soy; }
    public void setWhip(boolean whip) { this.whip = whip; }

    public void addMocha() {
        this.mocha = true;
        this.mochaCount++;
    }
}
