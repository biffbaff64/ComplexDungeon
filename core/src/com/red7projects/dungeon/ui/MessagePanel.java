package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.utils.logging.Trace;

public class MessagePanel
{
    private final String[] panels =
        {
            "message_panel1",
            "message_panel2",
            "message_panel3"
        };

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
        app.getHud().messageManager.addSlidePanel(panels[1]);
        app.getHud().messageManager.getCurrentPanel().set
            (
                new SimpleVec2F
                    (
                        (float) (Gfx._VIEW_WIDTH - app.getHud().messageManager.getCurrentPanelWidth()) / 2,
                        -app.getHud().messageManager.getCurrentPanelHeight()
                    ),
                new SimpleVec2F(0, app.getHud().messageManager.getCurrentPanelHeight() + 50),
                new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                new Speed(0, 40)
            );

        app.getHud().hideControls(false);
    }

    public boolean update()
    {
        return app.getHud().messageManager.doesPanelExist(panels[1]);
    }
}
