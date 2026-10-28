package com.systemdesign.lld.patterns.flyweight.example01.good;

import java.util.Objects;

/**
 * Context class storing extrinsic state (x, y coordinates).
 * Maintains a reference to the shared TreeType flyweight.
 */
public class Tree {
    private final int x;
    private final int y;
    private final TreeType type;

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = Objects.requireNonNull(type, "TreeType cannot be null");
    }

    public void draw(StringBuilder canvas) {
        type.render(x, y, canvas);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TreeType getType() {
        return type;
    }
}
