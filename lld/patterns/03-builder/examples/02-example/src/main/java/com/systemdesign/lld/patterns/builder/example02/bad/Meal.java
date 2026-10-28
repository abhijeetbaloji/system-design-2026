package com.systemdesign.lld.patterns.builder.example02.bad;

import java.util.List;

/**
 * BAD DESIGN:
 * Demonstrates telescoping constructors and mutable setters for constructing
 * complex restaurant meals.
 *
 * Flaws:
 * 1. Telescoping Constructor Nightmare:
 *    A constructor with 8+ parameters where strings (mainItem, sideItem, drinkItem,
 *    dessert, dietaryRequirement) are easily swapped.
 * 2. Inconsistent State via Mutability:
 *    A customer can request a "VEGAN" meal, but then callers can add real bacon
 *    or a dairy milkshake because there is no atomic invariant enforcement.
 * 3. Incomplete Meals:
 *    Using the empty constructor allows an order with zero items to be submitted
 *    to the kitchen.
 */
public class Meal {
    private String mainItem;
    private String sideItem;
    private String drinkItem;
    private String dessertItem;
    private String dietaryTag; // e.g. "VEGAN", "GLUTEN_FREE"
    private boolean isTakeout;
    private boolean includeToy;
    private double totalPrice;

    public Meal() {
    }

    public Meal(String mainItem, String sideItem, String drinkItem, String dessertItem,
                String dietaryTag, boolean isTakeout, boolean includeToy, double totalPrice) {
        this.mainItem = mainItem;
        this.sideItem = sideItem;
        this.drinkItem = drinkItem;
        this.dessertItem = dessertItem;
        this.dietaryTag = dietaryTag;
        this.isTakeout = isTakeout;
        this.includeToy = includeToy;
        this.totalPrice = totalPrice;
    }

    public String getMainItem() { return mainItem; }
    public void setMainItem(String mainItem) { this.mainItem = mainItem; }

    public String getSideItem() { return sideItem; }
    public void setSideItem(String sideItem) { this.sideItem = sideItem; }

    public String getDrinkItem() { return drinkItem; }
    public void setDrinkItem(String drinkItem) { this.drinkItem = drinkItem; }

    public String getDessertItem() { return dessertItem; }
    public void setDessertItem(String dessertItem) { this.dessertItem = dessertItem; }

    public String getDietaryTag() { return dietaryTag; }
    public void setDietaryTag(String dietaryTag) { this.dietaryTag = dietaryTag; }

    public boolean isTakeout() { return isTakeout; }
    public void setTakeout(boolean takeout) { isTakeout = takeout; }

    public boolean isIncludeToy() { return includeToy; }
    public void setIncludeToy(boolean includeToy) { this.includeToy = includeToy; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
