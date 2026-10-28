package com.systemdesign.lld.patterns.prototype.example02.bad;

import java.util.List;

/**
 * Flawed design: Character shallow copies equipment list and items.
 */
public class BadCharacter {
    public String name;
    public int health;
    public List<BadItem> equipment;

    public BadCharacter(String name, int health, List<BadItem> equipment) {
        this.name = name;
        this.health = health;
        this.equipment = equipment;
    }

    // Flawed: Shallow copy copies only pointer to equipment list!
    public BadCharacter shallowCopy() {
        return new BadCharacter(this.name, this.health, this.equipment);
    }
}
