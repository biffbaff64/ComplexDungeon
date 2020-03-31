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

package com.red7projects.dungeon.graphics.camera;

import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings("unused")
public class Zoom
{
    private static final float _DEFAULT_ZOOM = 0.0f;

    private       int     direction;
    private       float   zoomValue;
    private       float   resetValue;
    private       float   target;
    private final boolean bounce;

    public Zoom()
    {
        this.zoomValue  = _DEFAULT_ZOOM;
        this.resetValue = _DEFAULT_ZOOM;
        this.target     = Gfx._DEFAULT_ZOOM;
        this.bounce     = false;
        this.direction  = Movement._DIRECTION_STILL;
    }

    public boolean update(float zoom)
    {
        boolean done = false;

        if(direction == Movement._DIRECTION_UP)
        {
            if(zoomValue < target)
            {
                zoomValue += zoom;
            }
            else
            {
                if(bounce)
                {
                    direction = Movement._DIRECTION_DOWN;
                }

                done = true;
            }
        }
        else if(direction == Movement._DIRECTION_DOWN)
        {
            if(zoomValue > target)
            {
                zoomValue -= zoom;
            }
            else
            {
                if(bounce)
                {
                    direction = Movement._DIRECTION_UP;
                }

                done = true;
            }
        }

        return done;
    }

    public void stop()
    {
        zoomValue = resetValue;
    }

    public void in(float zoom)
    {
        zoomValue += zoom;
    }

    public void out(float zoom)
    {
        zoomValue -= zoom;
    }

    public float getZoomValue()
    {
        return zoomValue;
    }

    public void setZoomValue(float _zoom)
    {
        zoomValue = _zoom;
    }

    public void setTarget(final float _target)
    {
        this.target = _target;
    }

    public void setResetValue(float _reset)
    {
        resetValue = _reset;
    }
}
