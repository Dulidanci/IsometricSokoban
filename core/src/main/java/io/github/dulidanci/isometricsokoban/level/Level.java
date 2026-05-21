package io.github.dulidanci.isometricsokoban.level;

import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;


public class Level {
    public final int level;
    public final int width;
    public final int height;
    public final int length;
    private final Block[][][] map;

    private Level(Builder builder) {
        this.level = builder.level;
        this.width = builder.width;
        this.height = builder.height;
        this.length = builder.length;

        this.map = builder.map;
    }

    public ArrayList<Pair<BlockPos, Block>> renderOrder() {
        ArrayList<Pair<BlockPos, Block>> blocks = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (int k = 0; k < this.length; k++) {
                    if (map[i][j][k] != Blocks.AIR) {
                        blocks.add(Pair.of(new BlockPos(i, j, k), map[i][j][k]));
                    }
                }
            }
        }

        blocks.sort(Comparator.comparing(pair -> pair.getFirst().x() + pair.getFirst().y() + pair.getFirst().z()));

        return blocks;
    }

    public static class Builder {
        int level;
        int width;
        int height;
        int length;
        Block[][][] map;

        public Builder(int level, int width, int height, int length) {
            this.level = level;
            this.width = width;
            this.height = height;
            this.length = length;

            this.map = new Block[width][height][length];
        }

        public Builder addBlock(BlockPos blockPos, Block block) {
            if (blockPos.x() < 0 || blockPos.y() < 0 || blockPos.z() < 0 ||
                    blockPos.x() >= width || blockPos.y() >= height || blockPos.z() >= length) {
                throw new IllegalArgumentException("Block coordinate while building level is out of bounds");
            }
            map[blockPos.x()][blockPos.y()][blockPos.z()] = block;
            return this;
        }

        public Level build() {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < length; k++) {
                        if (map[i][j][k] == null) {
                            map[i][j][k] = Blocks.AIR;
                        }
                    }
                }
            }
            return new Level(this);
        }
    }
}
