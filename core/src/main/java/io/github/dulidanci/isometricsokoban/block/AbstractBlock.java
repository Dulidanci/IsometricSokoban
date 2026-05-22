package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.Direction;

public abstract class AbstractBlock {
    public final String id;

    public AbstractBlock(String id) {
        this.id = id;
    }

    public abstract boolean isSolid();
    public abstract boolean canBeMoved(Level level, BlockPos pos, Direction direction);
    public abstract boolean visible();
}
