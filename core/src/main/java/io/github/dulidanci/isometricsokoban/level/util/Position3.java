package io.github.dulidanci.isometricsokoban.level.util;

public record Position3(int x, int y, int z) {
    public Position3 add(Position3 other) {
        return new Position3(x + other.x, y + other.y, z + other.z);
    }

    public Position3 scale(int lambda) {
        return new Position3(x * lambda, y * lambda, z * lambda);
    }
}
