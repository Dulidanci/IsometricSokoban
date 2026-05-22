package io.github.dulidanci.isometricsokoban.level.util;

public record BlockPos(int x, int y, int z) {
    public BlockPos add(BlockPos other) {
        return new BlockPos(x + other.x, y + other.y, z + other.z);
    }
}
