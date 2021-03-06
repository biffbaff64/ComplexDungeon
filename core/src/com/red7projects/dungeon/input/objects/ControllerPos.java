/*
 *  Copyright 31/01/2019 Red7Projects.
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

import com.red7projects.dungeon.physics.Movement;

public enum ControllerPos
{
    _LEFT("LEFT", Movement._DIRECTION_LEFT),
    _RIGHT("RIGHT", Movement._DIRECTION_RIGHT),
    _HIDDEN("HIDDEN", Movement._DIRECTION_STILL);

    final int    value;
    final String text;

    ControllerPos(String _text, int _value)
    {
        text = _text;
        value = _value;
    }

    public String getText()
    {
        return text;
    }

    public int getValue()
    {
        return value;
    }
}
