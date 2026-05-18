package io.github.dulidanci.isometricsokoban.level.util;

public enum Direction {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);

    private final Position2 vector;

    Direction(int x, int y) {
        this.vector = new Position2(x, y);
    }

    public Position2 getVector() {
        return vector;
    }

    public Direction getOpposite() {
        return Direction.values()[(ordinal() + 2) % Direction.values().length];
    }
}
