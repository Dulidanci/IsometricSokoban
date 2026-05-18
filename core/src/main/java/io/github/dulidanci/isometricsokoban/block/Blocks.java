package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.registry.Registries;

public class Blocks {
    public static final Block AIR = register("air", new Block("air"));
    public static final Block BLOCK = register("block", new Block("block"));
    public static final Block BRICK = register("brick", new Block("brick"));
    public static final Block WALL = register("wall", new Block("wall"));
    public static final Block BOX = register("box", new Block("box"));

    public static Block register(String id, Block block) {
        return Registries.registerEntry(Registries.BLOCKS, id, block);
    }

    public static void init() {
        System.out.println("Initializing blocks");
    }
}
