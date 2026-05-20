package io.github.dulidanci.isometricsokoban.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.LevelLoader;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.util.Pair;

import java.util.ArrayList;

public class LevelScreen implements Screen {
    public final SpriteBatch batch;
    public final ExtendViewport viewport;
    public Level level;

    public LevelScreen() {
        batch = new SpriteBatch();
        viewport = new ExtendViewport(640, 480, new OrthographicCamera(640, 480));
        viewport.getCamera().position.set(320, 240, 0);
    }

    @Override
    public void show() {
        level = LevelLoader.load(0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.TEAL);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (level != null) {
            ArrayList<Pair<BlockPos, Block>> blockList = level.renderOrder();
            for (Pair<BlockPos, Block> pair : blockList) {
                batch.draw(
                    IsometricSokoban.getInstance().getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/" + pair.getSecond().id + ".png", Texture.class),
                    288 + pair.getFirst().x() * 32 - pair.getFirst().z() * 32,
                    208 - pair.getFirst().x() * 16 + pair.getFirst().y() * 32 - pair.getFirst().z() * 16,
                    2 * 32, 2 * 32
                );
            }
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;

        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        batch.dispose();
    }
}
