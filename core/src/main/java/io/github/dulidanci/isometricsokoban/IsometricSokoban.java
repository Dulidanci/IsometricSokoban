package io.github.dulidanci.isometricsokoban;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import io.github.dulidanci.isometricsokoban.screen.FirstScreen;
import io.github.dulidanci.isometricsokoban.screen.ScreenManager;

public class IsometricSokoban implements ApplicationListener {
    public static final String ID = "isometricsokoban";
    private static IsometricSokoban instance;
    private ScreenManager screenManager;
    private AssetManager assetManager;

    private IsometricSokoban() {
    }

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

//        assetManager.load(ID + "/textures/blocks/block.png", Texture.class);
//        assetManager.load(ID + "/textures/blocks/brick.png", Texture.class);
//        assetManager.load(ID + "/textures/blocks/wall.png", Texture.class);

        screenManager.setScreen(new FirstScreen());
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
