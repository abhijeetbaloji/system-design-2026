package com.systemdesign.lld.patterns.prototype.example02.good;

/**
 * Concrete Prototype representing a melee Warrior.
 */
public class Warrior extends GameCharacter {
    private final int armorRating;
    private int ragePoints;

    public Warrior(String name, int health, int level, int armorRating) {
        super(name, health, 0, level); // Warriors have 0 mana
        this.armorRating = armorRating;
        this.ragePoints = 0;
    }

    protected Warrior(Warrior source) {
        super(source); // Deep copies equipment, abilities, stats
        this.armorRating = source.armorRating;
        this.ragePoints = source.ragePoints;
    }

    @Override
    public Warrior clone() {
        return new Warrior(this);
    }

    @Override
    public int calculateTotalDefense() {
        return super.calculateTotalDefense() + armorRating;
    }

    public void generateRage(int points) {
        this.ragePoints = Math.min(100, this.ragePoints + points);
    }

    public int getArmorRating() {
        return armorRating;
    }

    public int getRagePoints() {
        return ragePoints;
    }

    @Override
    public String toString() {
        return String.format("Warrior['%s', HP: %d, Armor: %d, Rage: %d, Gear: %s, Abilities: %s]",
                name, health, armorRating, ragePoints, equipment, abilities);
    }
}
