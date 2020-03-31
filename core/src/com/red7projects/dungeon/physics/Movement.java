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

package com.red7projects.dungeon.physics;

//@formatter:off
public abstract class Movement
{
    public static final int _HORIZONTAL         = 1;
    public static final int _VERTICAL           = 2;
    public static final int _DIRECTION_IN       = 1;
    public static final int _DIRECTION_OUT      = -1;
    public static final int _FORWARDS           = 1;
    public static final int _BACKWARDS          = -1;
    public static final int _DIRECTION_RIGHT    = 1;
    public static final int _DIRECTION_LEFT     = -1;
    public static final int _DIRECTION_UP       = 1;
    public static final int _DIRECTION_DOWN     = -1;
    public static final int _DIRECTION_STILL    = 0;
    public static final int _DIRECTION_CUSTOM   = 2;

    public enum Dir
    {
        _STILL,
        _UP,
        _DOWN,
        _LEFT,
        _RIGHT,
        _UP_LEFT,
        _UP_RIGHT,
        _DOWN_LEFT,
        _DOWN_RIGHT
    }

    private static final String[][] aliases =
        {
            {"LEFT ", "STILL", "RIGHT"},
            {"DOWN ", "STILL", "UP   "},
        };

    public static String getAliasX(int _value)
    {
        return aliases[0][_value+1];
    }

    public static String getAliasY(int _value)
    {
        return aliases[1][_value+1];
    }

    public static Dir translateDirection(Direction direction)
    {
        final DirectionValue[] translateTable =
            {
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_STILL,   Dir._STILL),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_STILL,   Dir._LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_STILL,   Dir._RIGHT),
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_UP,      Dir._UP),
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_DOWN,    Dir._DOWN),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_UP,      Dir._UP_LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_UP,      Dir._UP_RIGHT),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_DOWN,    Dir._DOWN_LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_DOWN,    Dir._DOWN_RIGHT),
            };

        Dir translatedDir = Dir._STILL;

        for (DirectionValue directionValue : translateTable)
        {
            if ((directionValue.dirX == direction.getX()) && (directionValue.dirY == direction.getY()))
            {
                translatedDir = directionValue.translated;
            }
        }

        return translatedDir;
    }
}
