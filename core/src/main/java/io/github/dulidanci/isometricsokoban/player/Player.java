package io.github.dulidanci.isometricsokoban.player;

import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.Direction;
import io.github.dulidanci.isometricsokoban.level.util.Step;

public class Player {
    private BlockPos pos;
    private Direction direction;

    public Player(BlockPos pos) {
        this.pos = pos;
        this.direction = Direction.FORWARDS;
    }

    public boolean move(Level level, Direction direction) {
        if (level.validPosition(pos.add(direction.getVector()).add(Direction.DOWN.getVector())) && level.getBlock(pos.add(direction.getVector()).add(Direction.DOWN.getVector())).isSolid() && level.move(new Step(pos, direction))) {
            if (direction.isHorizontal()) {
                this.direction = direction;
            }
            pos = pos.add(direction.getVector());
            return true;
        }
        return false;
    }

    public void synchronizePosition(BlockPos pos) {
        this.pos = pos;
    }

    public Direction getDirection() {
        return direction;
    }
}
