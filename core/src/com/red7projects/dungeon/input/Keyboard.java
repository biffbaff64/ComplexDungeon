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

import com.badlogic.gdx.Input;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.game.App;

@SuppressWarnings("WeakerAccess")
public class Keyboard
{
    public boolean ctrlButtonHeld;
    private final App app;

    public Keyboard(App _app)
    {
        this.app = _app;
        ctrlButtonHeld = false;
    }

    public boolean maingameKeyDown(int keycode)
    {
        boolean returnFlag;

        if (keycode == AppConfig.defaultValueLeft)
        {
            app.getHud().buttonLeft.press();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueRight)
        {
            app.getHud().buttonRight.press();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueUp)
        {
            app.getHud().buttonUp.press();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueDown)
        {
            app.getHud().buttonDown.press();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueAttack)
        {
            app.getHud().buttonB.press();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueAction)
        {
            app.getHud().buttonA.press();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    app.getHud().buttonPause.press();

                    returnFlag = true;
                }
                break;

                case Input.Keys.STAR:
                {
                    if (Developer.isDevMode())
                    {
                        app.cameraUtils.resetCameraZoom();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.K:
                {
                    if (Developer.isDevMode())
                    {
                        app.getPlayer().kill();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.G:
                {
                    if (Developer.isDevMode())
                    {
                        app.gameProgress.gameCompleted = true;
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = true;
                    returnFlag = true;
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyUp(int keycode)
    {
        boolean returnFlag;

        if (keycode == AppConfig.defaultValueLeft)
        {
            app.getHud().buttonLeft.release();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueRight)
        {
            app.getHud().buttonRight.release();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueUp)
        {
            app.getHud().buttonUp.release();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueDown)
        {
            app.getHud().buttonDown.release();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueAttack)
        {
            app.getHud().buttonB.release();
            returnFlag = true;
        }
        else if (keycode == AppConfig.defaultValueAction)
        {
            app.getHud().buttonA.release();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    app.getHud().buttonPause.release();

                    returnFlag = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = false;
                    returnFlag = true;
                }
                break;

                case Input.Keys.NUM_1:
                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }
}
