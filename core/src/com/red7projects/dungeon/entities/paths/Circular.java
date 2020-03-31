/*
 *  Copyright 18/10/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.paths;

import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;

public class Circular implements PathMover
{
    private float timeInterval;
    private float xOffset;
    private float yOffset;

    public Circular()
    {
        reset();
    }

    public void reset()
    {
        timeInterval = 0;
        xOffset = 0;
        yOffset = 0;
    }

    public float getXOffset()
    {
        return xOffset;
    }

    public void setXOffset(float offset)
    {
        this.xOffset = offset;
    }

    public float getYOffset()
    {
        return yOffset;
    }

    public void setYOffset(float offset)
    {
        this.yOffset = offset;
    }

    /**
     * based on the current time interval, calculate
     * where the sphere is at on its orbit
     */
    public void setNextMove(GdxSprite gdxSprite, App app)
    {
        double radian = (Math.PI / 75) * timeInterval;

        gdxSprite.sprite.setX((float) ((gdxSprite.sprite.getOriginX() + xOffset) + ((gdxSprite.frameWidth * 2) * Math.cos(radian))));

        gdxSprite.sprite.setY((float) ((gdxSprite.sprite.getOriginY() + yOffset) + ((gdxSprite.frameHeight * 2) * Math.sin(radian))));

        timeInterval++;
    }
}
