package com.systemdesign.lld.patterns.prototype.example02.bad;

import java.util.ArrayList;
import java.util.List;

public class BadSpawnerDemo {
    public static void main(String[] args) {
        List<BadItem> orcGear = new ArrayList<>();
        orcGear.add(new BadItem("Iron Greatsword", 100));

        BadCharacter archetype = new BadCharacter("Orc Grunt", 200, orcGear);

        // Spawn 2 orcs via shallow copy
        BadCharacter orc1 = archetype.shallowCopy();
        BadCharacter orc2 = archetype.shallowCopy();

        // Orc 1 gets in combat and its sword takes 50 durability damage
        orc1.equipment.get(0).durability -= 50;

        System.out.println("Orc 1 sword durability: " + orc1.equipment.get(0).durability); // 50
        System.out.println("Orc 2 sword durability: " + orc2.equipment.get(0).durability); // BUG: 50!
        System.out.println("Archetype sword durability: " + archetype.equipment.get(0).durability); // BUG: 50!
    }
}
