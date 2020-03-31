/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.maths;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Vec2Boolean
{
    public boolean x;
    public boolean y;

    public Vec2Boolean()
    {
        this.x = false;
        this.y = false;
    }

    public Vec2Boolean(boolean _x, boolean _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void set(boolean _x, boolean _y)
    {
        this.x = _x;
        this.y = _y;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
