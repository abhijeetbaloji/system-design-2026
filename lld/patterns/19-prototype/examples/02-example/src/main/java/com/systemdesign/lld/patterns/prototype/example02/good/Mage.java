package com.systemdesign.lld.patterns.prototype.example02.good;

/**
 * Concrete Prototype representing a spellcasting Mage.
 */
public class Mage extends GameCharacter {
    private final int spellPower;
    private int manaShield;

    public Mage(String name, int health, int mana, int level, int spellPower) {
        super(name, health, mana, level);
        this.spellPower = spellPower;
        this.manaShield = 50;
    }

    protected Mage(Mage source) {
        super(source); // Deep copies equipment, abilities, stats
        this.spellPower = source.spellPower;
        this.manaShield = source.manaShield;
    }

    @Override
    public Mage clone() {
        return new Mage(this);
    }

    @Override
    public int calculateTotalAttack() {
        return super.calculateTotalAttack() + spellPower;
    }

    public void castSpell(int manaCost) {
        if (mana < manaCost) {
            throw new IllegalStateException("Not enough mana to cast spell!");
        }
        this.mana -= manaCost;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public int getManaShield() {
        return manaShield;
    }

    @Override
    public String toString() {
        return String.format("Mage['%s', HP: %d, MP: %d, SpellPower: %d, Shield: %d, Gear: %s, Abilities: %s]",
                name, health, mana, spellPower, manaShield, equipment, abilities);
    }
}
