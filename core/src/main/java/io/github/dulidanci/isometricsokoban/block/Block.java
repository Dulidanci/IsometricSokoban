package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.Step;

public class Block extends AbstractBlock {

    public Block(String id) {
        super(id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean canBeMoved(Level level, Step step) {
        return false;
    }

    @Override
    public boolean visible() {
        return true;
    }
}
