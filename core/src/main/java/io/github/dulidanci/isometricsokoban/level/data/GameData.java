package io.github.dulidanci.isometricsokoban.level.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;

public class GameData {
    private static final String DATA_FILE_PATH = "run/saves/save.json";
    private final LevelData[] CACHE;
    private boolean dirty;

    private GameData() {
        CACHE = new LevelData[IsometricSokoban.MAX_LEVEL_COUNT];
        dirty = false;
    }

    public static GameData loadData() {
        GameData gameData = new GameData();
        System.out.println("Loading save file");

        JsonValue root;
        try {
            root = new JsonReader().parse(Gdx.files.internal(DATA_FILE_PATH));
        } catch (Exception e) {
            System.out.println("Save file not existing. Creating new one");

            gameData.createEmptySaveFile();

            root = new JsonReader().parse(Gdx.files.internal(DATA_FILE_PATH));
        }

        for (int i = 0; i < IsometricSokoban.MAX_LEVEL_COUNT; i++) {
            gameData.CACHE[i] = new LevelData(
                root.get(i).getBoolean("unlocked"),
                root.get(i).getInt("moves")
            );
        }

        return gameData;
    }

    public void createEmptySaveFile() {
        LevelData[] data = new LevelData[IsometricSokoban.MAX_LEVEL_COUNT];

        for (int i = 0; i < IsometricSokoban.MAX_LEVEL_COUNT; i++) {
            data[i] = new LevelData(i == 0, -1);
        }

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        Gdx.files.local(DATA_FILE_PATH).writeString(json.prettyPrint(data), false);
    }

    public LevelData getLevelData(int level) {
        if (level < 0 || level >= IsometricSokoban.MAX_LEVEL_COUNT) {
            throw new IllegalArgumentException("Level number out of bounds when asking for LevelData!");
        }
        return CACHE[level];
    }

    public static void saveData(int level, int moves) {
        GameData gameData = IsometricSokoban.getInstance().getGameData();

        if (gameData.CACHE[level].moves < 0 || moves < gameData.CACHE[level].moves) {
            gameData.CACHE[level] = new LevelData(true, moves);
            gameData.markDirty();
        }
        if (level + 1 < IsometricSokoban.MAX_LEVEL_COUNT) {
            gameData.CACHE[level + 1] = new LevelData(true, gameData.CACHE[level + 1].moves);
            gameData.markDirty();
        }

        gameData.writeSave();
    }

    public void markDirty() {
        dirty = true;
    }

    public void writeSave() {
        if (dirty) {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);

            Gdx.files.local(DATA_FILE_PATH).writeString(json.prettyPrint(CACHE), false);
        }
        dirty = false;
    }

    public void unlockAll() {
        for (int i = 0; i < IsometricSokoban.MAX_LEVEL_COUNT; i++) {
            CACHE[i] = new LevelData(true, CACHE[i].moves);
        }
        markDirty();
        writeSave();
    }

    public record LevelData(boolean unlocked, int moves) {}
}
