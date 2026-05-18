package io.github.dulidanci.isometricsokoban.registry;

import io.github.dulidanci.isometricsokoban.block.Block;

public class Registries {
    public static final Registry<Block> BLOCKS = new Registry<>();

    public static <T> T registerEntry(Registry<T> registry, String key, T value) {
        return registry.register(key, value);
    }
}
