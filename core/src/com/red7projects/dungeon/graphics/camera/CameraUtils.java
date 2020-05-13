/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.red7projects.dungeon.graphics.camera;

import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;

public class CameraUtils
{
    private final App app;

    public CameraUtils(App _app)
    {
        this.app = _app;
    }

    public void resetCameraZoom()
    {
        app.baseRenderer.tiledGameCamera.camera.update();
        app.baseRenderer.spriteGameCamera.camera.update();
        app.baseRenderer.hudGameCamera.camera.update();

        app.baseRenderer.gameZoom.stop();
        app.baseRenderer.hudZoom.stop();

        app.baseRenderer.tiledGameCamera.camera.zoom    = Gfx._DEFAULT_ZOOM;
        app.baseRenderer.spriteGameCamera.camera.zoom   = Gfx._DEFAULT_ZOOM;
        app.baseRenderer.hudGameCamera.camera.zoom      = Gfx._DEFAULT_SCALE;
    }

    public void enableAllCameras()
    {
        app.baseRenderer.tiledGameCamera.isInUse    = true;
        app.baseRenderer.spriteGameCamera.isInUse   = true;
        app.baseRenderer.hudGameCamera.isInUse      = true;
        app.baseRenderer.isDrawingStage             = true;
    }

    public void disableAllCameras()
    {
        app.baseRenderer.tiledGameCamera.isInUse    = false;
        app.baseRenderer.spriteGameCamera.isInUse   = false;
        app.baseRenderer.hudGameCamera.isInUse      = false;
        app.baseRenderer.isDrawingStage             = false;
    }
}
