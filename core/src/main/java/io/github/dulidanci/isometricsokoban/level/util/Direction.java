package io.github.dulidanci.isometricsokoban.level.util;

public enum Direction {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);

    private final BlockPos vector;

    Direction(int x, int y) {
        this.vector = new BlockPos(x, y, 0);
    }

    public BlockPos getVector() {
        return vector;
    }

    public Direction getOpposite() {
        return Direction.values()[(ordinal() + 2) % Direction.values().length];
    }
}
