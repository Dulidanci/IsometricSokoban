package io.github.dulidanci.isometricsokoban;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.registry.Registries;
import io.github.dulidanci.isometricsokoban.screen.LevelScreen;
import io.github.dulidanci.isometricsokoban.screen.ScreenManager;

public class IsometricSokoban implements ApplicationListener {
    public static final String ID = "isometricsokoban";
    private static IsometricSokoban instance;
    private ScreenManager screenManager;
    private AssetManager assetManager;
    public static final int MAX_LEVEL_COUNT = 1;

    private IsometricSokoban() {}

    public static IsometricSokoban getInstance() {
        if (instance == null) {
            instance = new IsometricSokoban();
        }
        return instance;
    }

    @Override
    public void create() {
        screenManager = new ScreenManager();
        assetManager = new AssetManager();
        Blocks.init();

        Registries.BLOCKS.getAll().forEach(name -> assetManager.load(ID + "/textures/blocks/" + name + ".png", Texture.class));
        assetManager.finishLoading();

        screenManager.setScreen(new LevelScreen());
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void render() {
        screenManager.render();
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }

    @Override
    public void dispose() {
        screenManager.dispose();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
