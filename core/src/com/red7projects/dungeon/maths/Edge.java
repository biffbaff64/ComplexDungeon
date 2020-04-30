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

import com.badlogic.gdx.math.Rectangle;

public class Edge extends SimpleLine2D
{
    public Edge(float _x1, float _y1, float _x2, float _y2)
    {
        super(_x1, _y1, _x2, _y2);
    }

    public boolean intersectsRect(final Rectangle rectangle)
    {
        return intersectsRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public boolean intersectsRect(final float x, final float y, final float width, final float height)
    {
        boolean isIntersecting = false;

        if ((width > 0) && (height > 0))
        {
            if ((x1 >= x) && (x1 <= (x + width)) && (y1 >= y) && (y1 <= (y + height)))
            {
                isIntersecting = true;
            }

            if ((x2 >= x) && (x2 <= (x + width)) && (y2 >= y) && (y2 <= (y + height)))
            {
                isIntersecting = true;
            }

            if (linesIntersect(this, new SimpleLine2D(x, y, x, y + height))
                    || linesIntersect(this, new SimpleLine2D(x, y + height, x + width, y + height))
                    || linesIntersect(this, new SimpleLine2D(x + width, y, x + width, y + height))
                    || linesIntersect(this, new SimpleLine2D(x, y, x + width, y)))
            {
                isIntersecting = true;
            }
        }

        return isIntersecting;
    }

    private boolean linesIntersect(SimpleLine2D line1, SimpleLine2D line2)
    {
        float s1_x = line1.x2 - line1.x1;
        float s1_y = line1.y2 - line1.y1;
        float s2_x = line2.x2 - line2.x1;
        float s2_y = line2.y2 - line2.y1;

        final float v = (-s2_x * s1_y) + (s1_x * s2_y);

        float s = ((-s1_y * (line1.x1 - line2.x1)) + (s1_x * (line1.y1 - line2.y1))) / v;
        float t = ((s2_x * (line1.y1 - line2.y1)) - (s2_y * (line1.x1 - line2.x1))) / v;

        return ((s >= 0) && (s <= 1) && (t >= 0) && (t <= 1));
    }
}
