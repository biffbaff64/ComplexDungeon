/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@SuppressWarnings("unused")
class AnimationComponent
{
    private Animation       anim;
    private TextureRegion[] frames;

    private float elapsedTime;
    private int   frameWidth;
    private int   frameHeight;

    public Animation getAnim()
    {
        return anim;
    }

    public void setAnim(Animation anim)
    {
        this.anim = anim;
    }

    public TextureRegion[] getFrames()
    {
        return frames;
    }

    public void setFrames(TextureRegion[] frames)
    {
        this.frames = frames;
    }

    public float getElapsedTime()
    {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    public int getFrameWidth()
    {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth)
    {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight()
    {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight)
    {
        this.frameHeight = frameHeight;
    }
}
