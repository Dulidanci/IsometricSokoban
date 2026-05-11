package io.github.dulidanci.isometricsokoban.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    public final SpriteBatch batch;
    public final ExtendViewport viewport;
    public final Texture block;
    public final Texture brick;
    public final Texture wall;
    public final Texture box;

    public FirstScreen() {
        batch = new SpriteBatch();
        viewport = new ExtendViewport(640, 480, new OrthographicCamera(640, 480));
        viewport.getCamera().position.set(320, 240, 0);

        block = new Texture(IsometricSokoban.ID + "/textures/blocks/block.png");
        brick = new Texture(IsometricSokoban.ID + "/textures/blocks/brick.png");
        wall = new Texture(IsometricSokoban.ID + "/textures/blocks/wall.png");
        box = new Texture(IsometricSokoban.ID + "/textures/blocks/gift box.png");

//        block = instance.getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/block.png", Texture.class);
//        brick = instance.getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/brick.png", Texture.class);
//        wall = instance.getAssetManager().get(IsometricSokoban.ID + "/textures/blocks/wall.png", Texture.class);
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.

        ScreenUtils.clear(Color.TEAL);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                batch.draw(
                    wall, 192 - i * 64, 200 - i * 32 + j * 64, 4 * wall.getWidth(), 4 * wall.getHeight());
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                batch.draw(
                    wall, 320 + i * 64, 200 - i * 32 + j * 64, 4 * wall.getWidth(), 4 * wall.getHeight());
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                batch.draw(
                    (i + j) % 3 == 0 ? brick : block, 256 - i * 64 + j * 64, 168 - i * 32 - j * 32, 4 * block.getWidth(), 4 * block.getHeight());
            }
        }

        batch.draw(box, 320, 136, 4 * box.getWidth(), 4 * box.getHeight());

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.

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
        block.dispose();
        brick.dispose();
        wall.dispose();
    }
}
