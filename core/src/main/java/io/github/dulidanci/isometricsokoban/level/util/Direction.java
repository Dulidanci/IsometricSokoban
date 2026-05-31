package io.github.dulidanci.isometricsokoban.level.util;

public enum Direction {
    IDENTITY(0, 0, 0),
    RIGHT(1, 0, 0),
    UP(0, 1, 0),
    BACKWARDS(0, 0, 1),
    LEFT(-1, 0, 0),
    DOWN(0, -1, 0),
    FORWARDS(0, 0, -1);

    private final BlockPos vector;

    Direction(int x, int y, int z) {
        this.vector = new BlockPos(x, y, z);
    }

    public BlockPos getVector() {
        return vector;
    }

    public Direction getOpposite() {
        return Direction.values()[(ordinal() + 3) % Direction.values().length];
    }
}
