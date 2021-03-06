package com.red7projects.dungeon.ui;

import com.red7projects.dungeon.entities.characters.Villager;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

public class MessagePanel
{
    private final String[] panels =
        {
            "message_panel2",
            "message_panel1",
            "message_panel3"
        };

    private int panelIndex;
    private App app;

    public MessagePanel(App _app)
    {
        this.app = _app;
    }

    public void create()
    {
        Villager villager = (Villager) app.entityUtils.findNearest(GraphicID.G_VILLAGER);

        panelIndex = villager == null ? 0 : villager.villagerType;

        if (!app.getHud().messageManager.doesPanelExist(panels[panelIndex]))
        {
            UIUtils.setup(app);
            SimpleVec2F offset = UIUtils.getHUDOffset();

            app.getHud().messageManager.enable();
            app.getHud().messageManager.addSlidePanel(panels[panelIndex]);
            app.getHud().messageManager.getCurrentPanel().set
                (
                    new SimpleVec2F
                        (
                            offset.getX() + (float) ((Gfx._HUD_WIDTH - app.getHud().messageManager.getCurrentPanelWidth()) / 2),
                            offset.getY() - app.getHud().messageManager.getCurrentPanelHeight()
                        ),
                    new SimpleVec2F(0, app.getHud().messageManager.getCurrentPanelHeight() + Gfx.getTileHeight()),
                    new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                    new Speed(0, 40)
                );

            app.getHud().hideControls(false);
        }
    }

    public boolean update()
    {
        boolean isStillUpdating = isPanelActive();

        if (isStillUpdating)
        {
            if (app.getHud().messageManager.getCurrentPanel().getState() == StateID._UPDATE)
            {
                if (app.getHud().buttonX.isPressed())
                {
                    ((SlidePanel) app.getHud().messageManager.getCurrentPanel()).activate();

                    app.getHud().messageManager.closeSlidePanel();
                    app.getHud().buttonX.release();
                }
            }
        }

        return isStillUpdating;
    }

    public boolean isPanelActive()
    {
        return app.getHud().messageManager.doesPanelExist(panels[panelIndex]);
    }

    public void dispose()
    {
    }
}
