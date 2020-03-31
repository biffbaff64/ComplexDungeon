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

public class SimpleVec3F
{
    public float x;
    public float y;
    public float z;

    public SimpleVec3F()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public SimpleVec3F(float _x, float _y, float _z)
    {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public void add(float x, float y, float z)
    {
        this.set(this.x + x, this.y + y, this.z + z);
    }

    public void sub(float x, float y, float z)
    {
        this.set(this.x - x, this.y - y, this.z - z);
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(SimpleVec3F vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
        this.z = vec2.z;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0) && (z == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public float getX()
    {
        return x;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(final float y)
    {
        this.y = y;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(final float z)
    {
        this.z = z;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
