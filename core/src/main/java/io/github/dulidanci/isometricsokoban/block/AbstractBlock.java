package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.Step;

public abstract class AbstractBlock {
    public final String id;

    public AbstractBlock(String id) {
        this.id = id;
    }

    public abstract boolean isSolid();
    public abstract boolean canBeMoved(Level level, Step step);
    public abstract boolean visible();

    @Override
    public String toString() {
        return "Blocks." + id;
    }
}
