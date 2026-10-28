package com.systemdesign.lld.patterns.flyweight.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate container representing a forest of trees.
 */
public class Forest {
    private final List<Tree> trees = new ArrayList<>();
    private final TreeTypeFactory factory;

    public Forest(TreeTypeFactory factory) {
        this.factory = Objects.requireNonNull(factory, "Factory cannot be null");
    }

    public Forest() {
        this(new TreeTypeFactory());
    }

    public Tree plantTree(int x, int y, String name, String color, byte[] textureData) {
        TreeType type = factory.getTreeType(name, color, textureData);
        Tree tree = new Tree(x, y, type);
        trees.add(tree);
        return tree;
    }

    public void render(StringBuilder canvas) {
        for (Tree tree : trees) {
            tree.draw(canvas);
        }
    }

    public List<Tree> getTrees() {
        return Collections.unmodifiableList(trees);
    }

    public int getTotalTreeCount() {
        return trees.size();
    }

    public int getUniqueTypeCount() {
        return factory.getTypeCount();
    }
}
