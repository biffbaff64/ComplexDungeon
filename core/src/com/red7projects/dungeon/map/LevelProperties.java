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

package com.red7projects.dungeon.map;

public class LevelProperties
{
    public boolean  isActive;
    public boolean  isDisabled;
    public boolean  isNew;
    public boolean  isComingSoon;
    public int      levelNumber;
    public int      xpExpected;
    public int      xpTotal;
    public int      starsCount;

    public LevelProperties()
    {
        reset();
    }

    public LevelProperties(int level)
    {
        reset();

        this.levelNumber    = level;
    }

    public void reset()
    {
        this.isActive       = false;
        this.isDisabled     = false;
        this.isNew          = false;
        this.isComingSoon   = false;
        this.levelNumber    = 0;
        this.xpExpected     = 0;
        this.xpTotal        = 0;
        this.starsCount     = 0;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public boolean isDisabled()
    {
        return isDisabled;
    }

    public void enable()
    {
        this.isActive       = true;
        this.isDisabled     = false;
        this.xpTotal        = 0;
        this.starsCount     = 0;
    }

    public void disable()
    {
        this.isDisabled = true;
    }
}
