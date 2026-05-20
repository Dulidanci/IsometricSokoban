package io.github.dulidanci.isometricsokoban.level.util;

public record BlockPos(int x, int y, int z) {
    public BlockPos add(BlockPos other) {
        return new BlockPos(x + other.x, y + other.y, z + other.z);
    }

    public BlockPos scale(int lambda) {
        return new BlockPos(x * lambda, y * lambda, z * lambda);
    }
}
