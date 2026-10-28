package com.systemdesign.lld.patterns.builder.example02.good;

import java.util.Objects;

/**
 * Product Class:
 * Represents a fully configured, immutable combo meal order.
 *
 * Guarantees:
 * 1. Immutability: All fields are private final.
 * 2. Private Constructor: Accessible solely via Meal.Builder.
 * 3. Invariant Safety: Guarantees that dietary rules (e.g. VEGAN) and
 *    minimum requirements (must have at least a main dish) are satisfied.
 */
public class Meal {

    private final MenuItem mainItem;
    private final MenuItem sideItem;
    private final MenuItem drinkItem;
    private final MenuItem dessertItem;
    private final DietaryType dietaryType;
    private final boolean isTakeout;
    private final boolean includesToy;
    private final double totalPrice;

    private Meal(Builder builder) {
        this.mainItem = builder.mainItem;
        this.sideItem = builder.sideItem;
        this.drinkItem = builder.drinkItem;
        this.dessertItem = builder.dessertItem;
        this.dietaryType = builder.dietaryType;
        this.isTakeout = builder.isTakeout;
        this.includesToy = builder.includesToy;
        this.totalPrice = builder.computeTotalPrice();
    }

    public MenuItem getMainItem() { return mainItem; }
    public MenuItem getSideItem() { return sideItem; }
    public MenuItem getDrinkItem() { return drinkItem; }
    public MenuItem getDessertItem() { return dessertItem; }
    public DietaryType getDietaryType() { return dietaryType; }
    public boolean isTakeout() { return isTakeout; }
    public boolean includesToy() { return includesToy; }
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return "Meal{" +
            "main=" + (mainItem != null ? mainItem.name() : "None") +
            ", side=" + (sideItem != null ? sideItem.name() : "None") +
            ", drink=" + (drinkItem != null ? drinkItem.name() : "None") +
            ", dessert=" + (dessertItem != null ? dessertItem.name() : "None") +
            ", dietary=" + dietaryType +
            ", takeout=" + isTakeout +
            ", toy=" + includesToy +
            ", total=$" + String.format("%.2f", totalPrice) +
            '}';
    }

    /**
     * Fluent Builder for assembling Meals safely.
     */
    public static class Builder {
        private MenuItem mainItem;
        private MenuItem sideItem;
        private MenuItem drinkItem;
        private MenuItem dessertItem;
        private DietaryType dietaryType = DietaryType.REGULAR;
        private boolean isTakeout = false;
        private boolean includesToy = false;

        public Builder main(MenuItem mainItem) {
            this.mainItem = Objects.requireNonNull(mainItem, "Main item cannot be null");
            return this;
        }

        public Builder side(MenuItem sideItem) {
            this.sideItem = sideItem;
            return this;
        }

        public Builder drink(MenuItem drinkItem) {
            this.drinkItem = drinkItem;
            return this;
        }

        public Builder dessert(MenuItem dessertItem) {
            this.dessertItem = dessertItem;
            return this;
        }

        public Builder dietary(DietaryType dietaryType) {
            this.dietaryType = dietaryType != null ? dietaryType : DietaryType.REGULAR;
            return this;
        }

        public Builder takeout(boolean isTakeout) {
            this.isTakeout = isTakeout;
            return this;
        }

        public Builder withToy(boolean includesToy) {
            this.includesToy = includesToy;
            return this;
        }

        private double computeTotalPrice() {
            double raw = 0.0;
            if (mainItem != null) raw += mainItem.price();
            if (sideItem != null) raw += sideItem.price();
            if (drinkItem != null) raw += drinkItem.price();
            if (dessertItem != null) raw += dessertItem.price();

            // Combo discount: 10% off if customer orders Main + Side + Drink
            if (mainItem != null && sideItem != null && drinkItem != null) {
                raw *= 0.90; // 10% combo discount
            }
            return Math.round(raw * 100.0) / 100.0;
        }

        /**
         * Validates all business invariants before producing the Meal.
         */
        public Meal build() {
            // Invariant 1: Meal must have at least a main dish
            if (mainItem == null) {
                throw new IllegalStateException("A meal order cannot be built without a main dish");
            }

            // Invariant 2: Dietary safety check for VEGAN
            if (dietaryType == DietaryType.VEGAN) {
                if (!mainItem.isVegan()) {
                    throw new IllegalStateException("Cannot build a VEGAN meal with non-vegan main: " + mainItem.name());
                }
                if (sideItem != null && !sideItem.isVegan()) {
                    throw new IllegalStateException("Cannot build a VEGAN meal with non-vegan side: " + sideItem.name());
                }
                if (drinkItem != null && !drinkItem.isVegan()) {
                    throw new IllegalStateException("Cannot build a VEGAN meal with non-vegan drink: " + drinkItem.name());
                }
                if (dessertItem != null && !dessertItem.isVegan()) {
                    throw new IllegalStateException("Cannot build a VEGAN meal with non-vegan dessert: " + dessertItem.name());
                }
            }

            // Invariant 3: Dietary safety check for GLUTEN_FREE
            if (dietaryType == DietaryType.GLUTEN_FREE) {
                if (!mainItem.isGlutenFree()) {
                    throw new IllegalStateException("Cannot build a GLUTEN_FREE meal with non-gluten-free main: " + mainItem.name());
                }
                if (sideItem != null && !sideItem.isGlutenFree()) {
                    throw new IllegalStateException("Cannot build a GLUTEN_FREE meal with non-gluten-free side: " + sideItem.name());
                }
            }

            return new Meal(this);
        }
    }
}
