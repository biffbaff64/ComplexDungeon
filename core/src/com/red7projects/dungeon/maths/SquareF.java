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

@SuppressWarnings("unused")
public class SquareF
{
    public final float left;
    public final float right;
    public final float top;
    public final float bottom;

    public SquareF()
    {
        left = 0;
        right = 0;
        top = 0;
        bottom = 0;
    }

    public SquareF(float _left, float _top, float _right, float _bottom)
    {
        left = _left;
        right = _right;
        top = _top;
        bottom = _bottom;
    }

    @Override
    public String toString()
    {
        return "[" + left + ", " + top + "]" + "[" + right + ", " + bottom + "]";
    }
}
