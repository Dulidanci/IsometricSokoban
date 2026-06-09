package io.github.dulidanci.isometricsokoban.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.level.util.ChangeEntry;
import io.github.dulidanci.isometricsokoban.level.util.Direction;
import io.github.dulidanci.isometricsokoban.level.util.Step;
import io.github.dulidanci.isometricsokoban.player.Player;
import io.github.dulidanci.isometricsokoban.render.LevelSelectorWidget;
import io.github.dulidanci.isometricsokoban.render.Widget;
import io.github.dulidanci.isometricsokoban.screen.LevelScreen;
import io.github.dulidanci.isometricsokoban.screen.LevelSelectorScreen;
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
    private boolean paused;
    private boolean won;
    private boolean dead;
    private float wait;
    private final ArrayList<Widget<?>> normalWidgets = new ArrayList<>();
    private final ArrayList<Widget<?>> pauseWidgets = new ArrayList<>();
    private final ArrayList<Widget<?>> winWidgets = new ArrayList<>();
    private final ArrayList<ArrayList<ChangeEntry>> steps = new ArrayList<>();
    private final ArrayList<ChangeEntry> currentSteps = new ArrayList<>();
    private int moves;
    public final int SPIKE_ACTIVATION_TURNS;
    private boolean stabbing;


    private Level(Builder builder) {
        this.level = builder.level;
        this.width = builder.width;
        this.height = builder.height;
        this.length = builder.length;

        this.map = builder.map;
        this.player = builder.player;
        this.SPIKE_ACTIVATION_TURNS = builder.SPIKE_ACTIVATION_TURNS;

        this.paused = false;
        this.won = false;
        this.dead = false;
        this.wait = 0;
        this.moves = 0;
        this.stabbing = false;

        this.normalWidgets.add(new Widget<>(576, 416, 64, 64, "restart_button_32")
            .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(IsometricSokoban.getInstance().getScreenManager().getScreen())));
        this.normalWidgets.add(new Widget<>(512, 416, 64, 64, "undo_button")
            .setOnClick(t -> {
                if (!steps.isEmpty()) {
                    for (int i = steps.getLast().size() - 1; i >= 0; i--) {
                        setBlock(steps.getLast().get(i).blockPos(), steps.getLast().get(i).oldBlock());
                        if (steps.getLast().get(i).oldBlock() == Blocks.PLAYER) {
                            player.synchronizePosition(steps.getLast().get(i).blockPos());
                        }
                    }
                    steps.removeLast();
                    moves--;
                    stabbing = moves % SPIKE_ACTIVATION_TURNS == 0 && moves > 0;
                    dead = false;
                }
            }));

        this.pauseWidgets.add(new LevelSelectorWidget(184, 176, 272, 124, -1, "level_button"));
        this.pauseWidgets.add(new Widget<>(244, 184, 48, 48, "level_selector_button")
            .setVisible(false)
            .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(new LevelSelectorScreen())));
        this.pauseWidgets.add(new Widget<>(348, 184, 48, 48, "restart_button")
            .setVisible(false)
            .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(IsometricSokoban.getInstance().getScreenManager().getScreen())));

        boolean notLast = level + 1 < IsometricSokoban.MAX_LEVEL_COUNT;

        this.winWidgets.add(new LevelSelectorWidget(184, 176, 272, 128, -1, "level_button"));
        this.winWidgets.add(new Widget<>(notLast ? 218 : 244, 184, 48, 48, "level_selector_button")
            .setVisible(false)
            .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(new LevelSelectorScreen())));
        this.winWidgets.add(new Widget<>(notLast ? 296 : 348, 184, 48, 48, "restart_button")
            .setVisible(false)
            .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(IsometricSokoban.getInstance().getScreenManager().getScreen())));
        if (notLast) {
            this.winWidgets.add(new Widget<>(374, 184, 48, 48, "next_level_button")
                .setVisible(false)
                .setOnClick(t -> IsometricSokoban.getInstance().getScreenManager().setScreen(new LevelScreen(level + 1))));
        }
    }

    public void update(float delta, Vector2 mousePos) {
        currentSteps.clear();
        if (won && !dead) {
            if (wait < 1) {
                wait += delta;
            } else {
                winWidgets.forEach(widget -> widget.setVisible(true));
            }
            winWidgets.forEach(widget -> widget.update(mousePos));
        } else {
            if (paused) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    resume();
                }
                pauseWidgets.forEach(widget -> widget.update(mousePos));
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    pause();
                }
                if (!dead) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                        if (player.move(this, Direction.FORWARDS)) {
                            moves++;
                        }
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                        if (player.move(this, Direction.LEFT)) {
                            moves++;
                        }
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                        if (player.move(this, Direction.BACKWARDS)) {
                            moves++;
                        }
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                        if (player.move(this, Direction.RIGHT)) {
                            moves++;
                        }
                    }
                }

                if (!currentSteps.isEmpty()) {
                    if (moves % SPIKE_ACTIVATION_TURNS == 0 && moves > 0) {
                        stabbing = true;
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                for (int k = 0; k < length; k++) {
                                    if (map[i][j][k] == Blocks.SPIKE_BLOCK) {
                                        BlockPos pos = new BlockPos(i, j + 1, k);
                                        ChangeEntry entry = new ChangeEntry(pos, getBlock(pos), Blocks.SPIKES);
                                        if (getBlock(pos) == Blocks.PLAYER) {
                                            dead = true;
                                        }
                                        setBlock(pos, Blocks.SPIKES);
                                        currentSteps.add(entry);
                                    }
                                }
                            }
                        }
                    }
                    if (moves % SPIKE_ACTIVATION_TURNS == 1 && moves > 1) {
                        stabbing = false;
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                for (int k = 0; k < length; k++) {
                                    if (map[i][j][k] == Blocks.SPIKES) {
                                        BlockPos pos = new BlockPos(i, j, k);
                                        ChangeEntry entry = new ChangeEntry(pos, getBlock(pos), Blocks.AIR);
                                        setBlock(pos, Blocks.AIR);
                                        currentSteps.add(entry);
                                    }
                                }
                            }
                        }
                    }
                    steps.add(new ArrayList<>(currentSteps));
                }

                normalWidgets.forEach(widget -> widget.update(mousePos));

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
                    won = true;
                }
            }
        }
    }

    public boolean move(Step step) {
        if (validPosition(step.getTargetPos())) {
            if (validPosition(step.getTargetPos().add(Direction.DOWN.getVector())) && getBlock(step.getTargetPos().add(Direction.DOWN.getVector())).isSolid()) {
                if (!getBlock(step.getTargetPos()).isSolid() || (getBlock(step.getTargetPos()).isSolid()
                    && getBlock(step.getTargetPos()).canBeMoved(this, new Step(step.getTargetPos(), step.stepDirection())))) {

                    ChangeEntry target = new ChangeEntry(step.getTargetPos(), getBlock(step.getTargetPos()), getBlock(step.originalPos()));
                    setBlock(step.getTargetPos(), getBlock(step.originalPos()));
                    currentSteps.add(target);

                    ChangeEntry original = new ChangeEntry(step.originalPos(), getBlock(step.originalPos()), Blocks.AIR);
                    setBlock(step.originalPos(), Blocks.AIR);
                    currentSteps.add(original);

                    return true;
                }
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

    public void pause() {
        this.paused = true;
        pauseWidgets.forEach(widget -> widget.setVisible(true));
    }

    public void resume() {
        this.paused = false;
        pauseWidgets.forEach(widget -> widget.setVisible(false));
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        ArrayList<Pair<BlockPos, Block>> blocks = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (int k = 0; k < this.length; k++) {
                    if (map[i][j][k].visible()) {
                        blocks.add(Pair.of(new BlockPos(i, j, k), map[i][j][k]));
                        if (map[i][j][k] == Blocks.SPIKE_BLOCK && stabbing) {
                            blocks.add(Pair.of(new BlockPos(i, j, k).add(Direction.UP.getVector()), Blocks.SPIKES));
                        }
                    }
                }
            }
        }

        blocks.sort(Comparator.comparing(pair -> pair.getFirst().x() + pair.getFirst().y() + pair.getFirst().z()));

        for (Pair<BlockPos, Block> pair : blocks) {
            if (pair.getSecond() == Blocks.PLAYER) {
                Texture texture = IsometricSokoban.getInstance().getAssetManager().get(IsometricSokoban.ID + "/textures/player/player.png", Texture.class);
                batch.draw(texture,
                    302 + pair.getFirst().x() * 32 - pair.getFirst().z() * 32,
                    218 - pair.getFirst().x() * 16 + pair.getFirst().y() * 32 - pair.getFirst().z() * 16,
                    36, 44, 0, 0, texture.getWidth(), texture.getHeight(),
                    player.getDirection() == Direction.LEFT || player.getDirection() == Direction.BACKWARDS, false
                );
            } else {
                batch.draw(
                    IsometricSokoban.getInstance().getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/" + pair.getSecond().id + ".png", Texture.class),
                    288 + pair.getFirst().x() * 32 - pair.getFirst().z() * 32,
                    208 - pair.getFirst().x() * 16 + pair.getFirst().y() * 32 - pair.getFirst().z() * 16,
                    2 * 32, 2 * 32
                );
            }
        }

        font.draw(batch, "Level: " + (level + 1) + "\nMoves: " + moves + "\nBest solution: ", 32, 448);

        normalWidgets.forEach(widget -> widget.render(batch, font));

        if (dead) {
            batch.draw(IsometricSokoban.getInstance().getAssetManager().get(
                IsometricSokoban.ID + "/textures/widgets/dead.png", Texture.class),
                256, 416, 128, 64);
        }

        if (paused) {
            batch.draw(IsometricSokoban.getInstance().getAssetManager().get(
                IsometricSokoban.ID + "/textures/widgets/overlay.png", Texture.class),
                320 - (float) Gdx.graphics.getWidth() / 2, 240 - (float) Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            pauseWidgets.forEach(widget -> widget.render(batch, font));
            batch.draw(IsometricSokoban.getInstance().getAssetManager().get(
                IsometricSokoban.ID + "/textures/widgets/paused.png", Texture.class),
                192, 232, 256, 128);
        }

        if (won && wait >= 1) {
            batch.draw(IsometricSokoban.getInstance().getAssetManager().get(
                    IsometricSokoban.ID + "/textures/widgets/overlay.png", Texture.class),
                320 - (float) Gdx.graphics.getWidth() / 2, 240 - (float) Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            winWidgets.forEach(widget -> widget.render(batch, font));
            batch.draw(IsometricSokoban.getInstance().getAssetManager().get(
                    IsometricSokoban.ID + "/textures/widgets/you_win.png", Texture.class),
                192, 232, 256, 128);
        }
    }

    public static class Builder {
        public final int level;
        public final int width;
        public final int height;
        public final int length;
        public final Block[][][] map;
        public Player player;
        public int SPIKE_ACTIVATION_TURNS;

        public Builder(int level, int width, int height, int length) {
            this.level = level;
            this.width = width;
            this.height = height;
            this.length = length;

            this.map = new Block[width][height][length];
            this.setSpikeActivationTurns(5);
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

        public Builder setSpikeActivationTurns(int turns) {
            if (turns <= 0) {
                throw new IllegalArgumentException("Spike activation turns must be positive");
            }
            SPIKE_ACTIVATION_TURNS = turns;
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
