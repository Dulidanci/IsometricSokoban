package io.github.dulidanci.isometricsokoban.level.util;

import java.util.Objects;

public class Rectangle {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int width;
    private final int height;

    private Rectangle(int x1, int y1, int x2, int y2, int width, int height) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.height = height;
    }

    public static Rectangle fromPos(int x1, int y1, int x2, int y2) {
        return getNormalizedCorners(x1, y1, x2, y2);
    }

    public static Rectangle fromSize(int x1, int y1, int width, int height) {
        return getNormalizedCorners(x1, y1, x1 + width - 1, y1 + height - 1);
    }

    private static Rectangle getNormalizedCorners(int x1, int y1, int x2, int y2) {
        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2),
            Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    //todo: write a getter for a list of the positions inside the rectangle

    public boolean contains(int x, int y) {
        return  x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
    public String toString() {
        return "Rectangle[(" + x1 + ";" + y1 + "), (" + x2 + ";" + y2 + ")]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle rectangle)) return false;
        return Objects.equals(x1, rectangle.x1)
            && Objects.equals(y1, rectangle.y1)
            && Objects.equals(x2, rectangle.x2)
            && Objects.equals(y2, rectangle.y2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, x2, y2);
    }
}
