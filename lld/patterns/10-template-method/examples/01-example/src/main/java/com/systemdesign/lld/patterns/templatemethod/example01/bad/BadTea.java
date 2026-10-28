package com.systemdesign.lld.patterns.templatemethod.example01.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * Duplicates boiling and pouring logic found in BadCoffee.
 */
public class BadTea {

    private final List<String> steps = new ArrayList<>();

    public void prepareRecipe() {
        boilWater();
        steepTeaBag();
        pourInCup();
        addLemon();
    }

    private void boilWater() {
        // DUPLICATION of boilWater from BadCoffee
        steps.add("Boiling fresh spring water to 100°C");
        System.out.println("Boiling fresh spring water to 100°C");
    }

    private void steepTeaBag() {
        steps.add("Steeping Earl Grey tea bag for 3 minutes");
        System.out.println("Steeping Earl Grey tea bag for 3 minutes");
    }

    private void pourInCup() {
        // DUPLICATION of pourInCup from BadCoffee
        steps.add("Pouring into ceramic mug");
        System.out.println("Pouring into ceramic mug");
    }

    private void addLemon() {
        steps.add("Adding fresh lemon wedge and honey");
        System.out.println("Adding fresh lemon wedge and honey");
    }

    public List<String> getSteps() {
        return steps;
    }
}
