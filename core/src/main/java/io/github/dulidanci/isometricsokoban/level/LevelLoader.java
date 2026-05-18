package io.github.dulidanci.isometricsokoban.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.registry.Registries;

public class LevelLoader {

    public static Level load(int level) {
        if (level >= IsometricSokoban.MAX_LEVEL_COUNT || level < 0) {
            throw new IllegalArgumentException("Level count is out of bounds: " + level);
        }

        System.out.println("Loading level_" + level + ".json");
        JsonValue root = new JsonReader().parse(Gdx.files.internal(IsometricSokoban.ID + "/levels/level_" + level + ".json"));
        Level.Builder builder = new Level.Builder(
            level,
            root.get("size").getInt("length"),
            root.get("size").getInt("width"),
            root.get("size").getInt("height")
        );

        // iterating panels
        for (Level.MapLayer layer : Level.MapLayer.values()) {
            JsonValue panel = root.get(layer.name().toLowerCase());

            // reading default blocks pool
            JsonValue defaultPool = panel.get("default");
            Block[] blocks;
            if (defaultPool != null && defaultPool.isArray()) {
                blocks = new Block[defaultPool.size];
                for (int i = 0; i < defaultPool.size; i++) {
                    blocks[i] = Registries.BLOCKS.get(defaultPool.getString(i));
                }
            } else {
               blocks = new Block[]{Registries.BLOCKS.get("air")};
            }

            // adding default blocks to builder
            for (int i = 0; i < layer.x; i++) {
                for (int j = 0; j < layer.y; j++) {
                    builder.addBlock(layer, i, j, blocks[MathUtils.random(0, blocks.length - 1)]);
                }
            }

            // reading and adding features
            JsonValue features = panel.get("features");
            if (features != null && features.isArray()) {
                for (int i = 0; i < features.size; i++) {
                    JsonValue entry = features.get(i);
                    if (entry.getString("type").equals("block")) {
                        builder.addBlock(
                            layer,
                            entry.getInt("x"),
                            entry.getInt("y"),
                            Registries.BLOCKS.get(entry.getString("block"))
                        );
                    }
                }
            }
        }

        return builder.build();
    }
}
