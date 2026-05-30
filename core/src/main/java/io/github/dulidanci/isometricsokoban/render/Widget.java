package io.github.dulidanci.isometricsokoban.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;

import java.util.function.Consumer;

public class Widget<T extends Widget<T>> {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final String textureID;
    private Consumer<T> onClick;
    private boolean visible;

    public Widget(int x, int y, int width, int height, String textureID) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureID = textureID;
        this.visible = true;
    }

    @SuppressWarnings("unchecked")
    public T setOnClick(Consumer<T> onClick) {
        this.onClick = onClick;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setVisible(boolean visible) {
        this.visible = visible;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public void update(Vector2 mousePos) {
        if (visible) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (onClick != null) {
                    if (x <= mousePos.x && mousePos.x < x + width && y <= mousePos.y && mousePos.y < y + height) {
                        onClick.accept((T) this);
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        if (visible) {
            Texture texture = IsometricSokoban.getInstance().getAssetManager().get(
                IsometricSokoban.ID + "/textures/widgets/" + textureID + ".png", Texture.class
            );

            batch.draw(texture, x, y, width, height);
        }
    }
}
