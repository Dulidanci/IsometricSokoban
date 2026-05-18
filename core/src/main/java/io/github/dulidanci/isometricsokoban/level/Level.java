package io.github.dulidanci.isometricsokoban.level;

import io.github.dulidanci.isometricsokoban.block.Block;
import io.github.dulidanci.isometricsokoban.block.Blocks;
import io.github.dulidanci.isometricsokoban.level.util.Position3;
import io.github.dulidanci.isometricsokoban.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;


public class Level {
    public final int level;
    public final int length;
    public final int width;
    public final int height;
    private final EnumMap<MapLayer, Block[][]> map = new EnumMap<>(MapLayer.class);

    private Level(Builder builder) {
        this.level = builder.level;
        this.length = builder.length;
        this.width = builder.width;
        this.height = builder.height;

        map.clear();
        map.putAll(builder.map);
    }

    public ArrayList<Pair<Position3, Block>> renderOrder() {
        ArrayList<Pair<Position3, Block>> blocks = new ArrayList<>();

        for (MapLayer layer : MapLayer.values()) {
            if (map.containsKey(layer)) {
                for (int i = 0; i < layer.x; i++) {
                    for (int j = 0; j < layer.y; j++) {
                        if (map.get(layer)[i][j] != Blocks.AIR) {
                            switch (layer) {
                                    case RIGHT_WALL -> blocks.add(Pair.of(new Position3(i, j, 0), map.get(layer)[i][j]));
                                    case LEFT_WALL -> blocks.add(Pair.of(new Position3(0, j, i + 1), map.get(layer)[i][j]));
                                    case FLOOR -> blocks.add(Pair.of(new Position3(j + 1, 0, i + 1), map.get(layer)[i][j]));
                                    case BOARD -> blocks.add(Pair.of(new Position3(j + 1, 1, i + 1), map.get(layer)[i][j]));
                            }
                        }
                    }
                }
            }
        }

        blocks.sort(Comparator.comparing(pair -> pair.getFirst().x() + pair.getFirst().y() + pair.getFirst().z()));

        return blocks;
    }

    public enum MapLayer {
        RIGHT_WALL,
        LEFT_WALL,
        FLOOR,
        BOARD;

        int x;
        int y;

        static void prepare(int length, int width, int height) {
            RIGHT_WALL.x = width + 1;
            RIGHT_WALL.y = height + 1;
            LEFT_WALL.x = length;
            LEFT_WALL.y = height + 1;
            FLOOR.x = length;
            FLOOR.y = width;
            BOARD.x = length;
            BOARD.y = width;
        }
    }

    public static class Builder {
        int level;
        int length;
        int width;
        int height;
        EnumMap<MapLayer, Block[][]> map = new EnumMap<>(MapLayer.class);

        public Builder(int level, int length, int width, int height) {
            this.level = level;
            this.length = length;
            this.width = width;
            this.height = height;

            // Enum initialization
            MapLayer.prepare(length, width, height);

            // Map initialization
            map.put(MapLayer.RIGHT_WALL, new Block[MapLayer.RIGHT_WALL.x][MapLayer.RIGHT_WALL.y]);
            map.put(MapLayer.LEFT_WALL, new Block[MapLayer.LEFT_WALL.x][MapLayer.LEFT_WALL.y]);
            map.put(MapLayer.FLOOR, new Block[MapLayer.FLOOR.x][MapLayer.FLOOR.y]);
            map.put(MapLayer.BOARD, new Block[MapLayer.BOARD.x][MapLayer.BOARD.y]);
        }

        public Builder addBlock(MapLayer layer, int x, int y, Block block) {
            if (x < 0 || y < 0 || x >= layer.x || y >= layer.y) {
                throw new IllegalArgumentException("Block coordinate while building level is out of bounds");
            }
            map.get(layer)[x][y] = block;
            return this;
        }

        public Level build() {
            return new Level(this);
        }
    }
}
