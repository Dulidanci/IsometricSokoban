package io.github.dulidanci.isometricsokoban.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.Direction;
import io.github.dulidanci.isometricsokoban.player.Player;
import io.github.dulidanci.isometricsokoban.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;


public class Level {
    public final int level;
    public final int width;
    public final int height;
    public final int length;
    private final Block[][][] map;
    private final Player player;

    private Level(Builder builder) {
        this.level = builder.level;
        this.width = builder.width;
        this.height = builder.height;
        this.length = builder.length;

        this.map = builder.map;
        this.player = builder.player;
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.move(this, Direction.FORWARDS);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player.move(this, Direction.LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player.move(this, Direction.BACKWARDS);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player.move(this, Direction.RIGHT);
        }

        boolean fail = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < length; k++) {
                    if (map[i][j][k] == Blocks.TARGET && (!validPosition(new BlockPos(i, j + 1, k)) || map[i][j + 1][k] != Blocks.BOX)) {
                        fail = true;
                    }
                }
            }
        }

        if (!fail) {
            System.out.println("YAY, You won!");
        }
    }

    public boolean move(BlockPos pos, Direction direction) {
        BlockPos target = pos.add(direction.getVector());

        if (validPosition(target)) {
            if (!getBlock(target).isSolid() || (getBlock(target).isSolid() && getBlock(target).canBeMoved(this, target, direction))) {
                setBlock(target, getBlock(pos));
                setBlock(pos, Blocks.AIR);
                return true;
            }
        }

        return false;
    }

    public boolean validPosition(BlockPos pos) {
        return pos.x() >= 0 && pos.x() < this.width && pos.y() >= 0 && pos.y() < this.height && pos.z() >= 0 && pos.z() < this.length;
    }

    public Block getBlock(BlockPos pos) {
        return map[pos.x()][pos.y()][pos.z()];
    }

    public void setBlock(BlockPos pos, Block block) {
        map[pos.x()][pos.y()][pos.z()] = block;
    }

    public void render(SpriteBatch batch) {
        ArrayList<Pair<BlockPos, Block>> blocks = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (int k = 0; k < this.length; k++) {
                    if (map[i][j][k].visible()) {
                        blocks.add(Pair.of(new BlockPos(i, j, k), map[i][j][k]));
                    }
                }
            }
        }

        blocks.sort(Comparator.comparing(pair -> pair.getFirst().x() + pair.getFirst().y() + pair.getFirst().z()));

        for (Pair<BlockPos, Block> pair : blocks) {
            if (pair.getSecond() != Blocks.PLAYER) {
                batch.draw(
                    IsometricSokoban.getInstance().getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/" + pair.getSecond().id + ".png", Texture.class),
                    288 + pair.getFirst().x() * 32 - pair.getFirst().z() * 32,
                    208 - pair.getFirst().x() * 16 + pair.getFirst().y() * 32 - pair.getFirst().z() * 16,
                    2 * 32, 2 * 32
                );
            } else {
                batch.draw(IsometricSokoban.getInstance().getAssetManager().get(IsometricSokoban.ID + "/textures/player/player.png", Texture.class),
                    302 + pair.getFirst().x() * 32 - pair.getFirst().z() * 32,
                    218 - pair.getFirst().x() * 16 + pair.getFirst().y() * 32 - pair.getFirst().z() * 16,
                    36, 44
                );
            }
        }
    }

    public static class Builder {
        public final int level;
        public final int width;
        public final int height;
        public final int length;
        public final Block[][][] map;
        public Player player;

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

        public Builder setPlayer(BlockPos blockPos) {
            player = new Player(blockPos);
            map[blockPos.x()][blockPos.y()][blockPos.z()] = Blocks.PLAYER;
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
