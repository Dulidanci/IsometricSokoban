package io.github.dulidanci.isometricsokoban.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;

import java.util.function.Consumer;

public class Widget {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int number;
    private static TextureRegion[][] button = new TextureRegion[0][0];
    private Consumer<Widget> onClick;

    public Widget(int x, int y, int width, int height, int number) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.number = number;

        button = TextureRegion.split(IsometricSokoban.getInstance().getAssetManager().get(
            IsometricSokoban.ID + "/textures/widgets/button.png", Texture.class), 1, 1);
    }

    public Widget setOnClick(Consumer<Widget> onClick) {
        this.onClick = onClick;
        return this;
    }

    public void update(float delta, Vector2 mousePos) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (x <= mousePos.x && mousePos.x < x + width && y <= mousePos.y && mousePos.y < y + height) {
                onClick.accept(this);
            }
        }
    }

    public void render(float delta, SpriteBatch batch, BitmapFont font) {
        batch.draw(button[2][0], x, y, 4, 4);
        batch.draw(button[2][1], x + 4, y, width - 8, 4);
        batch.draw(button[2][2], x + width - 4, y, 4, 4);
        batch.draw(button[1][0], x, y + 4, 4, height - 8);
        batch.draw(button[1][1], x + 4, y + 4, width - 8, height - 8);
        batch.draw(button[1][2], x + width - 4, y + 4, 4, height - 8);
        batch.draw(button[0][0], x, y + height - 4, 4, 4);
        batch.draw(button[0][1], x + 4, y + height - 4, width - 8, 4);
        batch.draw(button[0][2], x + width - 4, y + height - 4, 4, 4);

        font.getData().setScale(2);
        font.draw(batch, String.valueOf(number + 1), x, y + (float) height * 0.7f, width, Align.center, false);
    }
}
