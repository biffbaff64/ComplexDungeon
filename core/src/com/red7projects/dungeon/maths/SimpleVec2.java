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

public class SimpleVec2
{
    protected int x;
    protected int y;

    public SimpleVec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public SimpleVec2(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void add(int _x, int _y)
    {
        this.set(this.x + _x, this.y + _y);
    }

    public void sub(int _x, int _y)
    {
        this.set(this.x - _x, this.y - _y);
    }

    public void set(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void set(SimpleVec2 vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
    }

    public int getX()
    {
        return x;
    }

    public void setX(final int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(final int y)
    {
        this.y = y;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
