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

package com.red7projects.dungeon.maths;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BoxF
{
    public float x;
    public float y;
    public float width;
    public float height;

    public BoxF()
    {
        this(0, 0, 0, 0);
    }

    public BoxF(BoxF box)
    {
        this(box.x, box.y, box.width, box.height);
    }

    public BoxF(Rectangle rectangle)
    {
        this((int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
    }

    public BoxF(float _width, float _height)
    {
        this(0, 0, _width, _height);
    }

    public BoxF(float _x, float _y, float _width, float _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public void set(float _x, float _y, float _width, float _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public boolean contains(float _x, float _y)
    {
        return (this.x <= _x) && ((this.x + this.width) >= _x)
            && (this.y <= _y) && ((this.y + this.height) >= _y);
    }

    public boolean contains(Vector2 point)
    {
        return contains(point.x, point.y);
    }

    public boolean contains(Rectangle rectangle)
    {
        float xmin = rectangle.x;
        float xmax = xmin + rectangle.width;

        float ymin = rectangle.y;
        float ymax = ymin + rectangle.height;

        return (((xmin > x) && (xmin < (x + width))) && ((xmax > x) && (xmax < (x + width))))
            && (((ymin > y) && (ymin < (y + height))) && ((ymax > y) && (ymax < (y + height))));
    }

    public Rectangle getRectangle()
    {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String toString()
    {
        return "[" + x + "," + y + "," + width + "," + height + "]";
    }
}
