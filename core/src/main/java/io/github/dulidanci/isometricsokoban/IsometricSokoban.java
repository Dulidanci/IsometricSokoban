package io.github.dulidanci.isometricsokoban;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.level.data.GameData;
import io.github.dulidanci.isometricsokoban.registry.Registries;
import io.github.dulidanci.isometricsokoban.screen.LevelSelectorScreen;
import io.github.dulidanci.isometricsokoban.screen.ScreenManager;

import java.util.Objects;

public class IsometricSokoban implements ApplicationListener {
    public static final String ID = "isometricsokoban";
    private static IsometricSokoban instance;
    private ScreenManager screenManager;
    private AssetManager assetManager;
    private GameData cachedData;
    public static final int MAX_LEVEL_COUNT = 12;

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

        Registries.BLOCKS.getAll().forEach(name ->
            assetManager.load(Objects.equals(name, "player") ?
                ID + "/textures/player/" + name + ".png" :
                ID + "/textures/blocks/" + name + ".png", Texture.class));
        assetManager.load(ID + "/textures/widgets/level_button.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/level_selector_button.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/restart_button.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/restart_button_32.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/undo_button.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/next_level_button.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/overlay.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/paused.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/you_win.png", Texture.class);
        assetManager.load(ID + "/textures/widgets/dead.png", Texture.class);
        assetManager.finishLoading();

        cachedData = GameData.loadData();

        screenManager.setScreen(new LevelSelectorScreen());
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
        assetManager.dispose();
        screenManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public GameData getGameData() {
        return cachedData;
    }

    public void reloadData() {
        cachedData = GameData.loadData();
    }
}
