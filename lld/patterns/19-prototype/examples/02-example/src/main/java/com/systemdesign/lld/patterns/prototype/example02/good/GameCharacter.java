package com.systemdesign.lld.patterns.prototype.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base Abstract Prototype representing a game character.
 * Features protected copy constructor performing deep copy of equipment items and abilities.
 */
public abstract class GameCharacter implements GamePrototype<GameCharacter> {
    protected String name;
    protected int health;
    protected int mana;
    protected int level;
    protected final List<Item> equipment = new ArrayList<>();
    protected final List<String> abilities = new ArrayList<>();

    public GameCharacter(String name, int health, int mana, int level) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.health = Math.max(1, health);
        this.mana = Math.max(0, mana);
        this.level = Math.max(1, level);
    }

    protected GameCharacter(GameCharacter source) {
        Objects.requireNonNull(source, "Source character cannot be null");
        this.name = source.name;
        this.health = source.health;
        this.mana = source.mana;
        this.level = source.level;

        // Strict Deep Copy: clone each item in equipment
        for (Item item : source.equipment) {
            this.equipment.add(item.clone());
        }

        // Deep copy abilities list
        this.abilities.addAll(source.abilities);
    }

    @Override
    public abstract GameCharacter clone();

    public void equip(Item item) {
        equipment.add(Objects.requireNonNull(item, "Item cannot be null"));
    }

    public void learnAbility(String ability) {
        abilities.add(Objects.requireNonNull(ability, "Ability cannot be null"));
    }

    public int calculateTotalAttack() {
        return equipment.stream().mapToInt(Item::getAttackBonus).sum();
    }

    public int calculateTotalDefense() {
        return equipment.stream().mapToInt(Item::getDefenseBonus).sum();
    }

    public void takeDamage(int damage) {
        int mitigated = Math.max(1, damage - calculateTotalDefense());
        this.health = Math.max(0, this.health - mitigated);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public int getLevel() {
        return level;
    }

    public List<Item> getEquipment() {
        return Collections.unmodifiableList(equipment);
    }

    public List<String> getAbilities() {
        return Collections.unmodifiableList(abilities);
    }
}
