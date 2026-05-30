package io.github.dulidanci.isometricsokoban.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.dulidanci.isometricsokoban.level.Level;
import io.github.dulidanci.isometricsokoban.level.LevelLoader;

public class LevelScreen implements Screen {
    public final SpriteBatch batch;
    public final BitmapFont font;
    public final ExtendViewport viewport;
    public int number;
    public Level level;

    public LevelScreen(int level) {
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new ExtendViewport(640, 480, new OrthographicCamera(640, 480));
        viewport.getCamera().position.set(320, 240, 0);
        number = level;
    }

    @Override
    public void show() {
        level = LevelLoader.load(number);
    }

    @Override
    public void render(float delta) {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        if (level != null) {
            level.update(delta, mousePos);
        }

        ScreenUtils.clear(Color.TEAL);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (level != null) {
            level.render(batch, font);
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
        level.pause();
    }

    @Override
    public void resume() {
        level.resume();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
    }
}
