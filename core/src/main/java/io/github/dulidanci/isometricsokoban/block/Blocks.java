package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.registry.Registries;

public class Blocks {
    public static final Block AIR = register("air", new Air("air"));
    public static final Block BLOCK = register("block", new Block("block"));
    public static final Block BRICK = register("brick", new Block("brick"));
    public static final Block WALL = register("wall", new Block("wall"));
    public static final Block BOX = register("box", new PushableBlock("box"));
    public static final Block PLAYER = register("player", new PushableBlock("player"));
    public static final Block TARGET = register("target", new Block("target"));
    public static final Block MOSSY_WALL = register("mossy_wall", new Block("mossy_wall"));
    public static final Block FLOWERY_WALL = register("flowery_wall", new Block("flowery_wall"));
    public static final Block SPIKE_BLOCK = register("spike_block", new Block("spike_block"));
    public static final Block SPIKES = register("spikes", new Air("spikes"));

    public static Block register(String id, Block block) {
        return Registries.registerEntry(Registries.BLOCKS, id, block);
    }

    public static void init() {
        System.out.println("Initializing blocks");
    }
}
