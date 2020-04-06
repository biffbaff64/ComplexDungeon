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

public abstract class ControllerMap
{
    public static float _MIN_RANGE;
    public static float _MAX_RANGE;
    public static float _DEAD_ZONE;

    public static int _BUTTON_X;
    public static int _BUTTON_Y;
    public static int _BUTTON_A;
    public static int _BUTTON_B;

    public static int _BUTTON_BACK;
    public static int _BUTTON_START;

    public static PovDirection _BUTTON_DPAD_UP;
    public static PovDirection _BUTTON_DPAD_DOWN;
    public static PovDirection _BUTTON_DPAD_RIGHT;
    public static PovDirection _BUTTON_DPAD_LEFT;

    public static int _BUTTON_LB;
    public static int _BUTTON_L3;
    public static int _BUTTON_RB;
    public static int _BUTTON_R3;

    public static int _AXIS_LEFT_X;
    public static int _AXIS_LEFT_Y;
    public static int _AXIS_RIGHT_X;
    public static int _AXIS_RIGHT_Y;

    public static int _AXIS_LEFT_TRIGGER;
    public static int _AXIS_RIGHT_TRIGGER;
    public static int _LEFT_TRIGGER;
    public static int _RIGHT_TRIGGER;

    public static boolean isInRange(final float value)
    {
        return ((value >= _MIN_RANGE) && (value <= _MAX_RANGE));
    }

    public static boolean isInNegativeRange(final float value)
    {
        return ((value <= -_DEAD_ZONE) && (value >= _MIN_RANGE));
    }

    public static boolean isInPositiveRange(final float value)
    {
        return ((value >= _DEAD_ZONE) && (value <= _MAX_RANGE));
    }
}
