package io.github.dulidanci.isometricsokoban;

import com.badlogic.gdx.Game;
import io.github.dulidanci.isometricsokoban.screen.FirstScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class IsometricSokoban extends Game {
    public static final String ID = "isometricsokoban";

    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
