/*
 *  Copyright 27/04/2018 Red7Projects.
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

package com.red7projects.dungeon.input;

import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;

public class TouchScreen
{
    private final App app;

    public TouchScreen(App _app)
    {
        this.app = _app;
    }

    public boolean titleScreenTouchDown(int screenX, int screenY)
    {
        return false;
    }

    public boolean titleScreenTouchUp(int screenX, int screenY)
    {
        return false;
    }

    public boolean gameScreenTouchDown(int screenX, int screenY, int pointer)
    {
        boolean returnFlag = false;

        if (app.getHud().buttonPause.contains(screenX, screenY))
        {
            app.getHud().buttonPause.press();
            returnFlag = true;
        }

        if (AppConfig.gamePaused)
        {
            if (app.getHud().pausePanel.buttonMusicVolume.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonMusicVolume.togglePressed();
                returnFlag = true;
            }

            if (app.getHud().pausePanel.buttonFXVolume.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonFXVolume.togglePressed();
                returnFlag = true;
            }

            if (app.getHud().pausePanel.buttonResume.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonResume.press();
                returnFlag = true;
            }

            if (app.getHud().pausePanel.buttonHome.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonHome.press();
                returnFlag = true;
            }
        }
        else
        {
            //
            // Debug Console or Developer Options Menu
            if (Developer.isDevMode())
            {
                if (app.getHud().buttonDevOptions != null)
                {
                    if (app.getHud().buttonDevOptions.contains(screenX, screenY))
                    {
                        app.getHud().buttonDevOptions.press();
                        returnFlag = true;
                    }
                }
            }

            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (((GameButton) app.getHud().buttonA).contains(screenX, screenY))
                {
                    app.getHud().buttonA.press();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonB).contains(screenX, screenY))
                {
                    app.getHud().buttonB.press();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonX).contains(screenX, screenY))
                {
                    app.getHud().buttonX.press();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonY).contains(screenX, screenY))
                {
                    app.getHud().buttonY.press();
                    returnFlag = true;
                }
            }
        }

        return returnFlag;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (app.getHud().buttonPause.contains(screenX, screenY))
        {
            app.getHud().buttonPause.release();
            returnFlag = true;
        }

        if (AppConfig.gamePaused)
        {
            if (app.getHud().pausePanel.buttonResume.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonResume.release();
                returnFlag = true;
            }

            if (app.getHud().pausePanel.buttonHome.contains(screenX, screenY))
            {
                app.getHud().pausePanel.buttonHome.release();
                returnFlag = true;
            }
        }
        else
        {
            //
            // Debug Console or Developer Options Menu
            if (Developer.isDevMode())
            {
                if (app.getHud().buttonDevOptions != null)
                {
                    if (app.getHud().buttonDevOptions.contains(screenX, screenY))
                    {
                        app.getHud().buttonDevOptions.release();
                        returnFlag = true;
                    }
                }
            }

            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (((GameButton) app.getHud().buttonB).contains(screenX, screenY))
                {
                    app.getHud().buttonB.release();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonA).contains(screenX, screenY))
                {
                    app.getHud().buttonA.release();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonX).contains(screenX, screenY))
                {
                    app.getHud().buttonX.release();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonY).contains(screenX, screenY))
                {
                    app.getHud().buttonY.release();
                    returnFlag = true;
                }
            }
        }

        return returnFlag;
    }
}
