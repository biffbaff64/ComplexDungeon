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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.types.ItemF;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class ProgressBar extends ItemF implements Disposable
{
    private static final int _DEFAULT_BAR_HEIGHT = 26;

    public       boolean justEmptied;
    public final boolean isAutoRefilling;

    private int   subInterval;
    private int   addInterval;
    private float speed;
    private float height;
    private float scale;

    private       NinePatch ninePatch;
    private final App   app;

    public ProgressBar(int _speed, int delay, int size, int maxSize, String texture, App _app)
    {
        this.app = _app;

        ninePatch = new NinePatch(_app.assets.getObjectsAtlas().findRegion(texture), 1, 1, 1, 1);

        this.minimum            = 0;
        this.maximum            = maxSize;
        this.refillAmount       = 0;
        this.stopWatch          = StopWatch.start();
        this.total              = size;
        this.height             = _DEFAULT_BAR_HEIGHT;
        this.refillAmount       = maxSize;
        this.speed              = _speed;
        this.subInterval        = delay;
        this.justEmptied        = false;
        this.isAutoRefilling    = false;
        this.scale              = 1;
    }

    public void draw(int x, int y)
    {
        if (total > 0)
        {
            ninePatch.draw(app.spriteBatch, x, y, total * scale, height);
        }
    }

    public void updateSlowDecrement()
    {
        justEmptied = false;

        if (total > 0)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= subInterval)
            {
                total -= speed;

                if (isEmpty())
                {
                    justEmptied = true;
                }

                stopWatch.reset();
            }
        }
    }

    public void updateSlowDecrementWithWrap(int wrap)
    {
        justEmptied = false;

        if (total > 0)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= subInterval)
            {
                total -= speed;
                total = Math.max(0, total);

                if (isEmpty())
                {
                    total = wrap;
                }

                stopWatch.reset();
            }
        }
    }

    public boolean updateSlowIncrement()
    {
        if (total < maximum)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= addInterval)
            {
                total += speed;

                stopWatch.reset();
            }
        }

        return isFull();
    }

    public void setHeight(float _height)
    {
        height = _height;
    }

    public void setScale(float newScale)
    {
        this.scale = newScale;
    }

    public float getScale()
    {
        return this.scale;
    }

    public void setColor(Color color)
    {
        this.ninePatch.setColor(color);
    }

    public void setSpeed(float _speed)
    {
        this.speed = _speed;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSubInterval(int _subInterval)
    {
        subInterval = _subInterval;
    }

    public void setAddInterval(int _addInterval)
    {
        addInterval = _addInterval;
    }

    @Override
    public void dispose()
    {
        ninePatch = null;
        stopWatch = null;
    }
}
