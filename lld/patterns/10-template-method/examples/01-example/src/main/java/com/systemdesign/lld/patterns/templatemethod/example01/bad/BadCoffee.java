package com.systemdesign.lld.patterns.templatemethod.example01.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * Coffee completely duplicates common beverage preparation steps (boiling water, pouring in cup).
 *
 * Flaws:
 * 1. Code Duplication:
 *    boilWater() and pourInCup() are copy-pasted across Coffee and Tea.
 * 2. Inflexible Recipe Sequence:
 *    No standardized framework or hook mechanism; changes to common workflow must be
 *    manually propagated to every beverage class.
 */
public class BadCoffee {

    private final List<String> steps = new ArrayList<>();

    public void prepareRecipe() {
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }

    private void boilWater() {
        steps.add("Boiling fresh spring water to 100°C");
        System.out.println("Boiling fresh spring water to 100°C");
    }

    private void brewCoffeeGrinds() {
        steps.add("Dripping coffee through filter");
        System.out.println("Dripping coffee through filter");
    }

    private void pourInCup() {
        steps.add("Pouring into ceramic mug");
        System.out.println("Pouring into ceramic mug");
    }

    private void addSugarAndMilk() {
        steps.add("Adding organic whole milk and cane sugar");
        System.out.println("Adding organic whole milk and cane sugar");
    }

    public List<String> getSteps() {
        return steps;
    }
}
