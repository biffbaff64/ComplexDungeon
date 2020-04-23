/*
 *  Copyright 22/05/2018 Red7Projects.
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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.UIButtons;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * The type IntroPanel.
 */
public class GameCompletedPanel implements Disposable
{
    private Texture     background;
    private ZoomPanel   zoomPanel;
    private StopWatch   stopWatch;
    private float       originX;
    private float       originY;

    private final App app;

    /**
     * Instantiates a new IntroPanel.
     *
     * @param _app the game
     */
    public GameCompletedPanel(App _app)
    {
        this.app = _app;
    }

    /**
     * Creates everything needed for the intro sequence.
     */
    public void setup()
    {
        originX = (app.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._VIEW_WIDTH / 2));
        originY = (app.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._VIEW_HEIGHT / 2));

        background = app.assets.loadSingleAsset("data/dark_screen.png", Texture.class);

        zoomPanel = new ZoomPanel();
        zoomPanel.initialise
            (
                app.assets.getObjectsAtlas().findRegion("completed_panel"),
                "ZoomPanel",
                true,
                true
            );

        stopWatch = StopWatch.start();

        UIButtons.fullScreenButton.release();
    }

    /**
     * Update the Intro sequence
     *
     * @return true when sequence is finished.
     */
    public boolean update()
    {
        if (zoomPanel.update() && (zoomPanel.getState() == StateID._STATE_ZOOM_OUT))
        {
            return true;
        }
        else
        {
            if (UIButtons.fullScreenButton.isPressed() || (stopWatch.time(TimeUnit.SECONDS) >= 30))
            {
                zoomPanel.setState(StateID._STATE_ZOOM_OUT);
            }
        }

        return false;
    }

    /**
     * Draw the intro messages.
     */
    public void draw()
    {
        app.spriteBatch.draw(background, originX, originY);

        zoomPanel.draw(app);
    }

    /**
     * Free up all resources used.
     */
    @Override
    public void dispose()
    {
        zoomPanel.dispose();
        zoomPanel = null;

        background = null;
        stopWatch = null;
    }
}
