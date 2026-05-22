package io.github.dulidanci.isometricsokoban.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;
import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.level.util.BlockPos;
import io.github.dulidanci.isometricsokoban.registry.Registries;

public class LevelLoader {

    public static Level load(int level) {
        if (level >= IsometricSokoban.MAX_LEVEL_COUNT || level < 0) {
            throw new IllegalArgumentException("Level count is out of bounds: " + level);
        }

        System.out.println("Loading level_" + level + ".json");
        JsonValue root;
        try {
            root = new JsonReader().parse(Gdx.files.internal(IsometricSokoban.ID + "/levels/level_" + level + ".json"));
        } catch (Exception e) {
            throw new RuntimeException("Unable to load level file " + level , e);
        }

        try {
            Level.Builder builder = new Level.Builder(
                level,
                root.get("size").getInt("width"),
                root.get("size").getInt("height"),
                root.get("size").getInt("length")
            );

            JsonValue pillars = root.get("pillars");
            if (pillars != null && pillars.isArray()) {
                for (JsonValue pillar : pillars) {
                    Block[] blocks = selectBlockPalette(root, pillar);

                    for (JsonValue position : pillar.get("positions")) {
                        for (int i = 0; i < builder.height; i++) {
                            builder.addBlock(new BlockPos(
                                position.getInt("x"),
                                i,
                                position.getInt("z")
                            ), blocks[MathUtils.random(blocks.length - 1)]);
                        }
                    }
                }
            }

            JsonValue cuboids = root.get("cuboids");
            if (cuboids != null && cuboids.isArray()) {
                for (JsonValue cuboid : cuboids) {
                    Block[] blocks = selectBlockPalette(root, cuboid);

                    int[] corners = new int[]{
                        cuboid.get("start").getInt("x"),
                        cuboid.get("start").getInt("y"),
                        cuboid.get("start").getInt("z"),
                        cuboid.get("end").getInt("x"),
                        cuboid.get("end").getInt("y"),
                        cuboid.get("end").getInt("z")
                    };

                    for (int i = Math.min(corners[0], corners[3]); i <= Math.max(corners[0], corners[3]); i++) {
                        for (int j = Math.min(corners[1], corners[4]); j <= Math.max(corners[1], corners[4]); j++) {
                            for (int k = Math.min(corners[2], corners[5]); k <= Math.max(corners[2], corners[5]); k++) {
                                builder.addBlock(new BlockPos(i, j, k), blocks[MathUtils.random(blocks.length - 1)]);
                            }
                        }
                    }
                }
            }

            JsonValue singles = root.get("singles");
            if (singles != null && singles.isArray()) {
                for (JsonValue single : singles) {
                    Block[] blocks = selectBlockPalette(root, single);

                    builder.addBlock(new BlockPos(
                        single.get("pos").getInt("x"),
                        single.get("pos").getInt("y"),
                        single.get("pos").getInt("z")
                    ),  blocks[MathUtils.random(blocks.length - 1)]);
                }
            }

            builder.setPlayer(new BlockPos(
                root.get("player").getInt("x"),
                root.get("player").getInt("y"),
                root.get("player").getInt("z")
            ));

            return builder.build();

        } catch (Exception e) {
            throw new RuntimeException("Error while loading level file " + level + " due to incorrect configuration." +
                " Check level file " + level, e);
        }
    }

    private static Block[] selectBlockPalette(JsonValue root, JsonValue currentObject) {
        Block[] blocks = new Block[0];

        if (currentObject.getString("blocks").equals("block")) {
            blocks = new Block[]{Registries.BLOCKS.get(currentObject.getString("block"))};

        } else if (currentObject.getString("blocks").equals("list")) {
            JsonValue list = currentObject.get("list");
            blocks = new Block[list.size];
            for (int i = 0; i < list.size; i++) {
                blocks[i] = Registries.BLOCKS.get(list.getString(i));
            }

        } else if (currentObject.getString("blocks").equals("pool")) {
            JsonValue pools = root.get("pools");
            for (JsonValue pool : pools) {
                if (pool.getString("name").equals(currentObject.getString("pool"))) {
                    blocks = new Block[pool.get("list").size];
                    for (int i = 0; i < pool.get("list").size; i++) {
                        blocks[i] = Registries.BLOCKS.get(pool.get("list").getString(i));
                    }
                }
            }
        }

        if (blocks.length == 0) {
            throw new RuntimeException("The block palette have not been initialised properly for object: \n" + currentObject);
        }
        return blocks;
    }
}
