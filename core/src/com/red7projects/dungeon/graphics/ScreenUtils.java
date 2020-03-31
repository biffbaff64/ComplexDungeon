/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 *
 *
 */

package com.red7projects.dungeon.graphics;

import com.badlogic.gdx.math.Rectangle;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.game.App;

public class ScreenUtils
{
    public final Rectangle viewportBox;
    public final Rectangle innerViewportBox;
    public final Rectangle extendedViewportBox;

	private final App app;

    public ScreenUtils(App _app)
    {
        Trace.__FILE_FUNC();

        app = _app;

        viewportBox = new Rectangle();
        innerViewportBox = new Rectangle();
        extendedViewportBox = new Rectangle();
    }

    /**
     * Update the screen virtual window.
     * This box is used for checking that entities are
     * visible on screen.
     */
    public void update()
    {
        float xPos          = app.baseRenderer.spriteGameCamera.camera.position.x;
        float yPos          = app.baseRenderer.spriteGameCamera.camera.position.y;
        float viewWidth     = app.baseRenderer.spriteGameCamera.camera.viewportWidth;
        float viewHeight    = app.baseRenderer.spriteGameCamera.camera.viewportHeight;
        float zoom          = app.baseRenderer.spriteGameCamera.camera.zoom;

        float x             = (xPos - ((viewWidth * zoom) / 2));
        float y             = (yPos - ((viewHeight * zoom) / 2));
        float width         = (viewWidth * zoom);
        float height        = (viewHeight * zoom);

        viewportBox.set(x, y, width, height);

        innerViewportBox.set
            (
                app.mapData.mapPosition.x,
                app.mapData.mapPosition.y,
                Gfx._VIEW_WIDTH,
                Gfx._VIEW_HEIGHT
            );

        extendedViewportBox.set
            (
                x - (width / 4),
                y - (height / 4),
                width + (width / 2),
                height + (height / 2)
            );
    }
}
