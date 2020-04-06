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

package com.red7projects.dungeon.input;

import com.red7projects.dungeon.physics.DirectionValue;
import com.red7projects.dungeon.physics.Movement;

public abstract class DirectionMap
{
    public static final DirectionValue[] map =
        {
/* 00 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 01 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 02 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 03 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_RIGHT),
/* 04 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_RIGHT),
/* 05 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_RIGHT),
/* 06 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 07 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 08 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 09 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 10 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 11 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_STILL,   Movement.Dir._RIGHT),
/* 12 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_RIGHT),
/* 13 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_RIGHT),
/* 14 */    new DirectionValue(Movement._DIRECTION_RIGHT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_RIGHT),
/* 15 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 16 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 17 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 18 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 19 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 20 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN),
/* 21 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_LEFT),
/* 22 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_LEFT),
/* 23 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_DOWN,   Movement.Dir._DOWN_LEFT),
/* 24 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 25 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 26 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 27 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 28 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 29 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_STILL,   Movement.Dir._LEFT),
/* 30 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_LEFT),
/* 31 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_LEFT),
/* 32 */    new DirectionValue(Movement._DIRECTION_LEFT,   Movement. _DIRECTION_UP,   Movement.Dir._UP_LEFT),
/* 33 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 34 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 35 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_UP,   Movement.Dir._UP),
/* 36 */    new DirectionValue(Movement._DIRECTION_STILL,   Movement. _DIRECTION_STILL,   Movement.Dir._STILL),
        };
}
