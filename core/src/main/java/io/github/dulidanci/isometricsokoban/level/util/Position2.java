package io.github.dulidanci.isometricsokoban.level.util;

public record Position2(int x, int y) {
    public Position2 add(Position2 other) {
        return new Position2(x + other.x, y + other.y);
    }

    public Position2 scale(int lambda) {
        return new Position2(x * lambda, y * lambda);
    }
}
