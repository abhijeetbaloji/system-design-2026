package good;

/**
 * Component interface.
 * Defines the contract for all beverages and condiment decorators alike.
 *
 * Both base coffees (ConcreteComponent) and add-ons (ConcreteDecorator)
 * conform to this abstraction, enabling transparent recursive wrapping.
 */
public interface Beverage {
    String getDescription();
    double getCost();
}
