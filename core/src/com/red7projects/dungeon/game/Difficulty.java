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

package com.red7projects.dungeon.game;

public enum Difficulty
{
    _EASY("EASY", 0, 0.5f),
    _NORMAL("NORMAL", 1, 1f),
    _HARD("HARD", 2, 2f);

    private final String text;
    private final float value;
    private final int index;

    Difficulty(String _text, int _index, float _value)
    {
        text = _text;
        index = _index;
        value = _value;
    }

    public static Difficulty setFromIndex(float index)
    {
        if (index == _HARD.value)
        {
            return _HARD;
        }
        else if (index == _NORMAL.value)
        {
            return _NORMAL;
        }
        else
        {
            return _EASY;
        }
    }

    public String getText()
    {
        return text;
    }

    public float getValue()
    {
        return value;
    }

    public int getIndex()
    {
        return index;
    }
}
