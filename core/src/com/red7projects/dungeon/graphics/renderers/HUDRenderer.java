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

package com.red7projects.dungeon.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.input.objects.ControllerType;

/**
 * Renderer for the HUD and also for MainMenuScreen child screens
 */
@SuppressWarnings("WeakerAccess")
public class HUDRenderer implements GameScreenRenderer
{
    private final App app;

    public HUDRenderer(App _app)
    {
        this.app = _app;
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera hudCamera)
    {
        if (!AppConfig.shutDownActive)
        {
            app.baseRenderer.hudZoom.stop();
            app.baseRenderer.gameZoom.stop();

            switch (app.appState.get())
            {
                case _STATE_TITLE_SCREEN:
                {
                    if (app.mainMenuScreen != null)
                    {
                        app.mainMenuScreen.draw(spriteBatch, hudCamera);
                    }
                }
                break;

                case _STATE_GET_READY:
                case _STATE_PAUSED:
                case _STATE_LEVEL_FINISHED:
                case _STATE_LEVEL_RETRY:
                case _STATE_GAME:
                case _STATE_MESSAGE_PANEL:
                case _STATE_DEBUG_HANG:
                case _STATE_ANNOUNCE_MISSILE:
                case _STATE_TELEPORTING:
                case _STATE_GAME_OVER:
                {
                    if (app.getHud() != null)
                    {
                        app.getHud().render((AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true)));
                    }
                }
                break;

                case _STATE_GAME_FINISHED:
                {
                    if (app.getHud() != null)
                    {
                        app.getHud().render(false);
                    }
                }
                break;

                case _STATE_CLOSING:
                default:
                {
                }
                break;
            }
        }
    }
}
