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

package com.red7projects.dungeon.map;

import com.red7projects.dungeon.entities.characters.JailKey;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class Room
{
    public static final int _N         = 0;
    public static final int _E         = 1;
    public static final int _S         = 2;
    public static final int _W         = 3;
    public static final int _START     = 4;
    public static final int _UNDEFINED = 5;

    public String        roomName;
    public SimpleVec2F[] compassPoints;
    public int           row;
    public int           column;
    public JailKey       key;
    public int           mysteryChestsAvailable;

    public Room()
    {
        this("");
    }

    public Room(final String _roomName)
    {
        this.roomName               = _roomName;
        this.key                    = new JailKey();
        this.mysteryChestsAvailable = 0;

        this.row    = 0;
        this.column = 0;

        this.compassPoints         = new SimpleVec2F[5];
        this.compassPoints[_N]     = new SimpleVec2F();
        this.compassPoints[_E]     = new SimpleVec2F();
        this.compassPoints[_S]     = new SimpleVec2F();
        this.compassPoints[_W]     = new SimpleVec2F();
        this.compassPoints[_START] = new SimpleVec2F();
    }

    public void set(Room _reference)
    {
        this.roomName        = _reference.roomName;
        this.key.isCollected = false;
        this.key.isUsed      = false;

        this.row    = _reference.row;
        this.column = _reference.column;

        this.compassPoints[_N]     = _reference.compassPoints[_N];
        this.compassPoints[_E]     = _reference.compassPoints[_E];
        this.compassPoints[_S]     = _reference.compassPoints[_S];
        this.compassPoints[_W]     = _reference.compassPoints[_W];
        this.compassPoints[_START] = _reference.compassPoints[_START];
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("roomName: " + roomName);
        Trace.dbg("row: " + row);
        Trace.dbg("column: " + column);
        Trace.dbg("compassPoints[_N]: " + compassPoints[_N]);
        Trace.dbg("compassPoints[_E]: " + compassPoints[_E]);
        Trace.dbg("compassPoints[_S]: " + compassPoints[_S]);
        Trace.dbg("compassPoints[_W]: " + compassPoints[_W]);
        Trace.dbg("compassPoints[_START]: " + compassPoints[_START]);
    }
}
