package io.github.dulidanci.isometricsokoban.player;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.Direction;

public class Player {
    private BlockPos pos;
    private Direction direction;

    public Player(BlockPos pos) {
        this.pos = pos;
        this.direction = Direction.BACKWARDS;
    }

    public void move(Level level, Direction direction) {
        if (level.move(pos, direction)) {
            this.direction = direction;
            pos = pos.add(direction.getVector());
        }
    }
}
