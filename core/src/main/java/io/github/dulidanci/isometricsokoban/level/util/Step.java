package io.github.dulidanci.isometricsokoban.level.util;

public record Step(BlockPos originalPos, Direction stepDirection) {
    public BlockPos getTargetPos() {
        return originalPos.add(stepDirection.getVector());
    }
}
