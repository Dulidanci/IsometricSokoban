package io.github.dulidanci.isometricsokoban.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import io.github.dulidanci.isometricsokoban.IsometricSokoban;

public class LevelSelectorWidget extends Widget<LevelSelectorWidget> {
    public final int number;
    private static TextureRegion[][] button = new TextureRegion[0][0];

    public LevelSelectorWidget(int x, int y, int width, int height, int number, String id) {
        super(x, y, width, height, id);

        this.number = number;

        button = TextureRegion.split(IsometricSokoban.getInstance().getAssetManager().get(
            IsometricSokoban.ID + "/textures/widgets/" + id + ".png", Texture.class), 1, 1);
    }

    @Override
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
