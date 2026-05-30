package io.github.dulidanci.isometricsokoban.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.render.LevelSelectorWidget;

import java.util.ArrayList;

public class LevelSelectorScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final ExtendViewport viewport;
    private final ArrayList<LevelSelectorWidget> widgets = new ArrayList<>();

    public LevelSelectorScreen() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new ExtendViewport(640, 480, new OrthographicCamera(640, 480));
        viewport.getCamera().position.set(320, 240, 0);

        for (int i = 0; i < IsometricSokoban.MAX_LEVEL_COUNT; i++) {
            widgets.add(new LevelSelectorWidget(48 + (i % 6) * 96, 400 - (i / 6) * 96, 64, 64, i, "level_button")
                .setOnClick(this::onClick)
            );
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        for (LevelSelectorWidget levelSelectorWidget : widgets) {
            levelSelectorWidget.update(mousePos);
        }

        ScreenUtils.clear(Color.TEAL);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        for (LevelSelectorWidget levelSelectorWidget : widgets) {
            levelSelectorWidget.render(batch, font);
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        widgets.clear();
    }

    public void onClick(LevelSelectorWidget widget) {
            IsometricSokoban.getInstance().getScreenManager().setScreen(new LevelScreen(widget.number));
    }
}
