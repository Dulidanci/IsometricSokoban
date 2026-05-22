package io.github.dulidanci.isometricsokoban.block;

public class Air extends Block {

    public Air(String id) {
        super(id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean visible() {
        return false;
    }
}
