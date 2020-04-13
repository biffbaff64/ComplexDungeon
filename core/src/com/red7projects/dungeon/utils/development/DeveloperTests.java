/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.utils.development;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.StringBuilder;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.UIButtons;

public class DeveloperTests
{
    private TextureRegion title;
    StringBuilder controllerName;
    StringBuilder buttonPress;
    StringBuilder axisCode;
    StringBuilder povPress;
    private final App app;

    public DeveloperTests(App _app)
    {
        super();

        this.app = _app;
    }

    public void setup()
    {
        controllerName = new StringBuilder();
        buttonPress    = new StringBuilder();
        axisCode       = new StringBuilder();
        povPress       = new StringBuilder();

        title = createTitle();
    }

    public void updatePanel()
    {
        controllerName.clear();
        buttonPress.clear();
        axisCode.clear();
        povPress.clear();

        if (AppConfig.controllersFitted)
        {
            try
            {
                controllerName.append("Controller Name: ").append(app.inputManager.gameController.getController().getName());
                buttonPress.append("Button Code: ").append(UIButtons.controllerButtonCode);
                axisCode.append("Axis Code/Value: ").append(UIButtons.controllerAxisCode).append("    /    ").append(UIButtons.controllerAxisValue);
                povPress.append("POV Code: ").append(UIButtons.controllerPovDirection);
            }
            catch (NullPointerException npe)
            {
                controllerName.append("Controller Name: Name Error, possible corruption...");
            }
        }
        else
        {
            controllerName.append("Controller Name: No External Controller Detected");
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(title, 0, 0);

        DebugRenderer.drawText(controllerName.toString(), 40, (Gfx._VIEW_HEIGHT - 380));
        DebugRenderer.drawText(buttonPress.toString(), 40, (Gfx._VIEW_HEIGHT - 420));
        DebugRenderer.drawText(axisCode.toString(), 40, (Gfx._VIEW_HEIGHT - 460));
        DebugRenderer.drawText(povPress.toString(), 40, (Gfx._VIEW_HEIGHT - 500));
    }

    public void clearUp()
    {
        controllerName = null;
        buttonPress    = null;
        axisCode       = null;
        povPress       = null;
        title          = null;
    }

    /**
     * Create the Title image
     *
     * @return Image holding the created title
     */
    private TextureRegion createTitle()
    {
        return app.assets.getTextAtlas().findRegion("test_title");
    }
}
