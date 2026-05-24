package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.Step;

public class PushableBlock extends Block {

    public PushableBlock(String id) {
        super(id);
    }

    @Override
    public boolean canBeMoved(Level level, Step step) {
        return level.move(step);
    }
}
