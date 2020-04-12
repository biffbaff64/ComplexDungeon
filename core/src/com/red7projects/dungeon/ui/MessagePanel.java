package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

public class MessagePanel
{
    private TextureRegion villagerHead;
    private App app;

    public MessagePanel(App _app)
    {
        this.app = _app;
    }

    public void create()
    {
        Trace.__FILE_FUNC();

        app.getHud().messageManager.enable();
        app.getHud().messageManager.addSlidePanel(GameAssets._MESSAGE_PANEL_ASSET);
        app.getHud().messageManager.getCurrentPanel().set
            (
                new SimpleVec2F
                    (
                        (float) (Gfx._VIEW_WIDTH - app.getHud().messageManager.getCurrentPanelWidth()) / 2,
                        -app.getHud().messageManager.getCurrentPanelHeight()
                    ),
                new SimpleVec2F(0, app.getHud().messageManager.getCurrentPanelHeight() + 50),
                new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                new Speed(0, (float) ((app.getHud().messageManager.getCurrentPanelHeight() + 50) / 10))
            );

        app.getHud().hideControls(false);
    }

    public boolean update()
    {
        return false;
    }
}
