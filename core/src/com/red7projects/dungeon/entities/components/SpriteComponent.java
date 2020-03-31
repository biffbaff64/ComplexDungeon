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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.graphics.GraphicID;

public class SpriteComponent
{
    private Sprite    sprite       = new Sprite();
    private GraphicID gid          = GraphicID.G_NO_ID;
    private Actions   spriteAction = Actions._NO_ACTION;
    private float     rotateSpeed  = 0.0f;
    private boolean   isRotating   = false;
    private boolean   isDrawable   = false;
    private boolean   isFlippedX   = false;
    private boolean   isFlippedY   = false;

    public Sprite getSprite()
    {
        return sprite;
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
    }

    public GraphicID getGid()
    {
        return gid;
    }

    public void setGid(GraphicID gid)
    {
        this.gid = gid;
    }

    public Actions getSpriteAction()
    {
        return spriteAction;
    }

    public void setSpriteAction(Actions spriteAction)
    {
        this.spriteAction = spriteAction;
    }

    public float getRotateSpeed()
    {
        return rotateSpeed;
    }

    public void setRotateSpeed(float rotateSpeed)
    {
        this.rotateSpeed = rotateSpeed;
    }

    public boolean isRotating()
    {
        return isRotating;
    }

    public void setRotating(boolean rotating)
    {
        isRotating = rotating;
    }

    public boolean isDrawable()
    {
        return isDrawable;
    }

    public void setDrawable(boolean drawable)
    {
        isDrawable = drawable;
    }

    public boolean isFlippedX()
    {
        return isFlippedX;
    }

    public void setFlippedX(boolean flippedX)
    {
        isFlippedX = flippedX;
    }

    public boolean isFlippedY()
    {
        return isFlippedY;
    }

    public void setFlippedY(boolean flippedY)
    {
        isFlippedY = flippedY;
    }
}
