package io.github.dulidanci.isometricsokoban.block;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.Direction;

public class PushableBlock extends Block {

    public PushableBlock(String id) {
        super(id);
    }

    @Override
    public boolean canBeMoved(Level level, BlockPos pos, Direction direction) {
        return level.move(pos, direction);
    }
}
