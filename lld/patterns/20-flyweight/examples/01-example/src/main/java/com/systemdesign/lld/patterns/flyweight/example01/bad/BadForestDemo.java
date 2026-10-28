package com.systemdesign.lld.patterns.flyweight.example01.bad;

import java.util.ArrayList;
import java.util.List;

public class BadForestDemo {
    public static void main(String[] args) {
        List<BadTree> trees = new ArrayList<>();
        // In bad design, every tree allocates its own separate 64KB texture
        for (int i = 0; i < 1000; i++) {
            byte[] heavyTexture = new byte[64 * 1024]; // 64 KB allocated 1000 times = 64 MB!
            trees.add(new BadTree(i, i * 2, "Oak", "Green", heavyTexture));
        }
        System.out.println("Planted 1000 bad trees consuming ~64 MB of redundant texture buffers!");
    }
}
