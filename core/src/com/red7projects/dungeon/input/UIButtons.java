/*
 *  Copyright 24/04/2018 Red7Projects.
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

import com.badlogic.gdx.controllers.PovDirection;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.buttons.Switch;

// TODO: 12/12/2019 - This class confuses me, WHY do i have this?

@SuppressWarnings({"unused", "WeakerAccess"})
public class UIButtons
{
    public static Switch        fullScreenButton;
    public static Switch        systemBackButton;

    public static int           controllerButtonCode;
    public static int           controllerAxisCode;
    public static float         controllerAxisValue;
    public static PovDirection  controllerPovDirection;

    public static boolean       controllerStartPressed;
    public static boolean       controllerFirePressed;
    public static boolean       controllerAPressed;
    public static boolean       controllerBPressed;
    public static boolean       controllerXPressed;
    public static boolean       controllerYPressed;
    public static boolean       controllerLBPressed;
    public static boolean       controllerRBPressed;
    public static boolean       controllerBackPressed;
    public static boolean       controllerLeftFirePressed;
    public static boolean       controllerRightFirePressed;
    public static boolean       controllerUpPressed;
    public static boolean       controllerDownPressed;
    public static boolean       controllerLeftPressed;
    public static boolean       controllerRightPressed;

    public static void setup(App app)
    {
        fullScreenButton = new Switch(0, 0, Gfx._VIEW_WIDTH, Gfx._VIEW_HEIGHT, app);

        controllerAPressed          = false;
        controllerBPressed          = false;
        controllerXPressed          = false;
        controllerYPressed          = false;
        controllerLBPressed         = false;
        controllerRBPressed         = false;
        controllerStartPressed      = false;
        controllerBackPressed       = false;
        controllerLeftFirePressed   = false;
        controllerRightFirePressed  = false;
        controllerFirePressed       = false;

        controllerButtonCode        = -1;
        systemBackButton            = new Switch(app);
    }

    public static void releaseAll()
    {
        if (fullScreenButton != null)   { fullScreenButton.release(); }
        if (systemBackButton != null)   { systemBackButton.release(); }
    }
}
