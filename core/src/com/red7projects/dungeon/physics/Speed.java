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

import com.red7projects.dungeon.maths.SimpleVec2F;
import org.jetbrains.annotations.NotNull;

public class Speed extends SimpleVec2F
{
    public Speed()
    {
        super();
    }

    public Speed(float x, float y)
    {
        super(x, y);
    }

    public Speed(Speed _speed)
    {
        set(_speed);
    }

    public boolean isEmpty()
    {
        return ((x == 0f) && (y == 0f));
    }

    public void set(@NotNull Speed refSpeed)
    {
        this.x = refSpeed.x;
        this.y = refSpeed.y;
    }
}
