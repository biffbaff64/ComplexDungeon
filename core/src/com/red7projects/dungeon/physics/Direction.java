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

package com.red7projects.dungeon.physics;

import com.red7projects.dungeon.maths.SimpleVec2;
import org.jetbrains.annotations.NotNull;

public class Direction extends SimpleVec2
{
    public Direction()
    {
        this.x = 0;
        this.y = 0;
    }

    public Direction(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public Direction(Direction _direction)
    {
        this.x = _direction.x;
        this.y = _direction.y;
    }

    public void set(Direction _direction)
    {
        this.x = _direction.x;
        this.y = _direction.y;
    }

    public void standStill()
    {
        this.x = Movement._DIRECTION_STILL;
        this.y = Movement._DIRECTION_STILL;
    }

    public boolean hasDirection()
    {
        return (this.x != Movement._DIRECTION_STILL) || (this.y != Movement._DIRECTION_STILL);
    }

    public int getFlippedX()
    {
        return this.x * -1;
    }

    public int getFlippedY()
    {
        return this.y * -1;
    }

    public void toggle()
    {
        if (this.x != Movement._DIRECTION_STILL)
        {
            toggleX();
        }

        if (this.y != Movement._DIRECTION_STILL)
        {
            toggleY();
        }
    }

    public void toggleX()
    {
        this.x *= -1;
    }

    public void toggleY()
    {
        this.y *= -1;
    }

    @NotNull
    @Override
    public String toString()
    {
        return Movement.getAliasX(x) + ", " + Movement.getAliasY(y);
    }
}
