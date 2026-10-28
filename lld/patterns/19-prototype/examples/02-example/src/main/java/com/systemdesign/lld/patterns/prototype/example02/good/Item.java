package com.systemdesign.lld.patterns.prototype.example02.good;

import java.util.Objects;

/**
 * Equipment item component implementing deep copy.
 */
public class Item implements GamePrototype<Item> {
    private final String name;
    private final int attackBonus;
    private final int defenseBonus;
    private int durability;

    public Item(String name, int attackBonus, int defenseBonus, int durability) {
        this.name = Objects.requireNonNull(name, "Item name cannot be null");
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.durability = Math.max(0, durability);
    }

    public Item(Item source) {
        Objects.requireNonNull(source, "Source item cannot be null");
        this.name = source.name;
        this.attackBonus = source.attackBonus;
        this.defenseBonus = source.defenseBonus;
        this.durability = source.durability;
    }

    @Override
    public Item clone() {
        return new Item(this);
    }

    public void takeDamage(int amount) {
        this.durability = Math.max(0, this.durability - amount);
    }

    public String getName() {
        return name;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public String toString() {
        return String.format("%s (ATK: +%d, DEF: +%d, Durability: %d%%)",
                name, attackBonus, defenseBonus, durability);
    }
}
