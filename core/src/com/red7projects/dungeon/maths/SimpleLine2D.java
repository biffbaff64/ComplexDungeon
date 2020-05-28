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

package com.red7projects.dungeon.maths;

public class SimpleLine2D
{
    public final float x1;
    public final float y1;
    public final float x2;
    public final float y2;

    public SimpleLine2D(float _x1, float _y1, float _x2, float _y2)
    {
        this.x1 = _x1;
        this.y1 = _y1;
        this.x2 = _x2;
        this.y2 = _y2;
    }

    public boolean intersects(SimpleLine2D line2)
    {
        float s1_x = x2 - x1;
        float s1_y = y2 - y1;
        float s2_x = line2.x2 - line2.x1;
        float s2_y = line2.y2 - line2.y1;

        final float v = (-s2_x * s1_y) + (s1_x * s2_y);

        float s = ((-s1_y * (x1 - line2.x1)) + (s1_x * (y1 - line2.y1))) / v;
        float t = ((s2_x * (y1 - line2.y1)) - (s2_y * (x1 - line2.x1))) / v;

        return ((s >= 0) && (s <= 1) && (t >= 0) && (t <= 1));
    }
}
