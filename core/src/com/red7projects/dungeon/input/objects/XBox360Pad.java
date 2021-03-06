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

package com.red7projects.dungeon.input.objects;

import com.badlogic.gdx.controllers.PovDirection;

public class XBox360Pad implements ControlMapData
{
    /*
     * It seems there are different versions of gamepads with different ID Strings.
     * Therefore its IMO a better bet to check for:
     * if (controller.getName().toLowerCase().contains("xbox") &&
     *             controller.getName().contains("360"))
     *
     * Controller (Gamepad for Xbox 360)
     * Controller (XBOX 360 For Windows)
     * Controller (Xbox 360 Wireless Receiver for Windows)
     * Controller (Xbox wireless receiver for windows)
     * XBOX 360 For Windows (Controller)
     * Xbox 360 Wireless Receiver
     * Xbox Receiver for Windows (Wireless Controller)
     * Xbox wireless receiver for windows (Controller)
     */

    public static final float           _MIN_RANGE              = -1.0f;
    public static final float           _MAX_RANGE              = 1.0f;
    public static final float           _DEAD_ZONE              = 0.5f;

    public static final int             _BUTTON_X               = 2;
    public static final int             _BUTTON_Y               = 3;
    public static final int             _BUTTON_A               = 0;
    public static final int             _BUTTON_B               = 1;

    public static final int             _BUTTON_BACK            = 6;
    public static final int             _BUTTON_START           = 7;

    public static final PovDirection    _BUTTON_DPAD_UP         = PovDirection.north;
    public static final PovDirection    _BUTTON_DPAD_DOWN       = PovDirection.south;
    public static final PovDirection    _BUTTON_DPAD_RIGHT      = PovDirection.east;
    public static final PovDirection    _BUTTON_DPAD_LEFT       = PovDirection.west;
    public static final PovDirection    _BUTTON_DPAD_CENTRE     = PovDirection.center;

    public static final int             _BUTTON_LB              = 4;
    public static final int             _BUTTON_L3              = 8;
    public static final int             _BUTTON_RB              = 5;
    public static final int             _BUTTON_R3              = 9;

    public static final int             _AXIS_LEFT_X            = 1; // -1 is left | +1 is right
    public static final int             _AXIS_LEFT_Y            = 0; // -1 is up | +1 is down
    public static final int             _AXIS_RIGHT_X           = 3; // -1 is left | +1 is right
    public static final int             _AXIS_RIGHT_Y           = 2; // -1 is up | +1 is down
    public static final int             _AXIS_LEFT_TRIGGER      = 4; // value 0 to 1f
    public static final int             _AXIS_RIGHT_TRIGGER     = 4; // value 0 to -1f

    public static final int _LEFT_TRIGGER = 4;
    public static final int _RIGHT_TRIGGER = 4;
}
