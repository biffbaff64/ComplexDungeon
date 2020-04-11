package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;

public class MessagePanel extends SlidePanel
{
    private App app;

    public MessagePanel(App _app)
    {
        this.app = _app;
    }

    public void create()
    {
    }

    public boolean update()
    {
        return false;
    }
}
