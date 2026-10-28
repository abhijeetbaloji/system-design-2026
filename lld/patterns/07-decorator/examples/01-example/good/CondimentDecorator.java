package good;

import java.util.Objects;

/**
 * Abstract Decorator class conforming to the Beverage interface.
 *
 * Key Decorator characteristics:
 * 1. Dual relationship: Implements Beverage (type conformance) and aggregates
 * a Beverage instance (composition).
 * 2. Immutable wrapper: The wrapped reference is protected and final.
 * 3. Delegation base: Subclasses delegate calls down the chain and append
 * their own condiment behavior.
 */
public abstract class CondimentDecorator implements Beverage {
    protected final Beverage beverage;

    protected CondimentDecorator(Beverage beverage) {
        this.beverage = Objects.requireNonNull(beverage, "Beverage to wrap cannot be null");
    }

    @Override
    public abstract String getDescription();

    @Override
    public abstract double getCost();
}
