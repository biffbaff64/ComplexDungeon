/*
 *  Copyright 20/06/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

public class MessageManager implements Disposable
{
    public boolean messageActive;

    private       UserInterfacePanel currentPanel;
    private       boolean managerEnabled;
    private final App     app;

    public MessageManager(App _app)
    {
        this.app = _app;

        messageActive   = false;
        managerEnabled  = false;
    }

    public void addSlidePanel(String imageName)
    {
        if (managerEnabled)
        {
            Trace.__FILE_FUNC(imageName);

            SlidePanel panel = new SlidePanel();

            panel.initialise(app.assets.getObjectsAtlas().findRegion(imageName), imageName);
            panel.activate();
            panel.action = Actions._OPENING;

            if (currentPanel != null)
            {
                currentPanel.forceZoomOut();
            }
            else
            {
                currentPanel = panel;
            }
        }
    }

    public void closeSlidePanel()
    {
        if (currentPanel.getState() == StateID._UPDATE)
        {
            currentPanel.set
                (
                    new SimpleVec2F(currentPanel.getPosition().x, currentPanel.getPosition().y),
                    new SimpleVec2F(0, currentPanel.getSize().getY() + 50),
                    new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    new Speed(0, 40)
                );

            currentPanel.setState(StateID._STATE_CLOSING);
        }
    }

    public void addZoomMessage(String imageName, int displayDelay)
    {
        if (managerEnabled)
        {
            Trace.__FILE_FUNC(imageName);

            UserInterfacePanel panel = new ZoomPanel();

            if (app.assets.getTextAtlas().findRegion(imageName) == null)
            {
                Trace.__FILE_FUNC("ERROR: " + imageName + " not loaded!");
            }

            panel.initialise
                (
                    app.assets.getTextAtlas().findRegion(imageName),
                    imageName,
                    /* _canPause   */(displayDelay > 0),
                    /* _bounceBack */ true
                );
            panel.setPauseTime(displayDelay);

            if (currentPanel != null)
            {
                currentPanel.forceZoomOut();
            }
            else
            {
                currentPanel = panel;
            }
        }
    }

    public void addZoomMessage(String _fileName, int _delay, int x, int y)
    {
        enable();
        addZoomMessage(_fileName, _delay);
        setPosition(_fileName, x, y);
    }

    /**
     * Update the current message.
     * The current message is always the
     * one at position zero.
     */
    public void updateMessage()
    {
        if (currentPanel != null)
        {
            messageActive = !currentPanel.update();

            if (!messageActive)
            {
                disable();
                currentPanel.dispose();
                currentPanel = null;
            }
        }
    }

    /**
     * Draw the current message.
     * The current message is always the
     * one at position zero.
     */
    public void draw()
    {
        if (currentPanel != null)
        {
            currentPanel.draw(app);
        }
    }

    public boolean doesPanelExist(String _nameID)
    {
        boolean exists = false;

        if (currentPanel != null)
        {
            if (currentPanel.nameExists(_nameID))
            {
                exists = true;
            }
        }

        return exists;
    }

    public UserInterfacePanel getCurrentPanel()
    {
        return currentPanel;
    }

    public int getCurrentPanelWidth()
    {
        return currentPanel.getSize().getX();
    }

    public int getCurrentPanelHeight()
    {
        return currentPanel.getSize().getY();
    }

    public void setPosition(String _nameID, int x, int y)
    {
        if ((currentPanel != null) && doesPanelExist(_nameID))
        {
            currentPanel.setPosition(x, y);
        }
    }

    public void enable()
    {
        managerEnabled = true;
    }

    public void disable()
    {
        managerEnabled = false;
    }

    public boolean isEnabled()
    {
        return managerEnabled;
    }

    @Override
    public void dispose()
    {
//        panel.dispose();
//        panel = null;
    }
}
