/*
 *  Copyright 27/04/2018 Red7Projects.
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

package com.red7projects.dungeon.input.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GfxUtils;
import com.red7projects.dungeon.maths.Box;
import com.red7projects.dungeon.physics.Movement;
import org.jetbrains.annotations.NotNull;

public class GameButton extends BasicButton implements Disposable
{
    private static final int _INITIAL_ROTATION  = 270;
    private static final int _INITIAL_DISTANCE  = 45;
    private static final int _ROTATE_SPEED      = 1;
    private static final int _INITIAL_DIRECTION = Movement._DIRECTION_RIGHT;

    public TextureRegion bg;
    public TextureRegion bgPressed;
    public TextureRegion bgDisabled;

    public boolean forceShowPressed;
    public boolean isRotating;
    public boolean isScaling;
    public boolean isToAndFro;
    public boolean isDrawable;
    public boolean isClockwise;

    public float scaleMinimum;
    public float scaleMaximum;
    public float scale;
    public float buttonTimer;
    public float alpha;
    public float scaleSpeed;

    public int direction;
    public int rotation;
    public int rotateDir;
    public int rotateDist;
    public int rotateSpeed;

    private int mapIndex;

    /**
     * Define a GameButton
     *
     * @param textureRegion        - Image used for default state
     * @param textureRegionPressed - Image used for PRESSED state
     * @param x                    - X Display co-ordinate
     * @param y                    - Y Display co-ordinate
     * @param _app                 - Instance of the game
     */
    public GameButton(TextureRegion textureRegion, TextureRegion textureRegionPressed, int x, int y, ButtonID _id, App _app)
    {
        this(x, y, _id, _app);

        this.bg         = textureRegion;
        this.bgPressed  = textureRegionPressed;
        this.width      = textureRegion.getRegionWidth();
        this.height     = textureRegion.getRegionHeight();
        this.alpha      = 1.0f;
        this.isDrawable = true;
        this.buttonRect = new Box(this.x, this.y, this.width, this.height);
    }

    /**
     * Define a GameButton
     *
     * @param x    - X Display co-ordinate
     * @param y    - Y Display co-ordinate
     * @param _app - Instance of the game
     */
    public GameButton(int x, int y, ButtonID _id, App _app)
    {
        super(_app, _id);

        this.bg               = null;
        this.bgPressed        = null;
        this.bgDisabled       = null;
        this.x                = x;
        this.y                = y;
        this.width            = 0;
        this.height           = 0;
        this.isDrawable       = false;
        this.forceShowPressed = false;
        this.isRotating       = false;
        this.rotation         = _INITIAL_ROTATION;
        this.rotateDir        = _INITIAL_DIRECTION;
        this.rotateDist       = _INITIAL_DISTANCE;
        this.rotateSpeed      = 1;
        this.isScaling        = false;
        this.scaleMaximum     = 1.3f;
        this.scaleMinimum     = 0.7f;
        this.scale            = 0.95f;
        this.direction        = Movement._DIRECTION_STILL;
        this.buttonTimer      = 0;
        this.isToAndFro       = false;
        this.pointer          = 0;

        mapIndex = app.inputManager.gameButtons.size;

        app.inputManager.gameButtons.add(this);
    }

    public void delete()
    {
        app.inputManager.gameButtons.removeIndex(mapIndex);
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera)
    {
        if ((isActive || isDisabled) && isDrawable)
        {
            TextureRegion textureRegion = (isPressed || forceShowPressed) ? bgPressed : bg;

            if (isDisabled)
            {
                textureRegion = bgDisabled;
            }

            spriteBatch.draw
                (
                    textureRegion,
                    (camera.position.x + (float) (x - (Gfx._VIEW_WIDTH / 2))),
                    (camera.position.y + (float) (y - (Gfx._VIEW_HEIGHT / 2))),
                    width,
                    height
                );

            if (AppConfig.canDrawButtonBoxes)
            {
                GfxUtils.drawRect(x, y, width, height, 2, Color.WHITE);
            }
        }
    }

    public void setTextureRegion(TextureRegion textureRegion)
    {
        this.bg = textureRegion;

        this.width             = this.bg.getRegionWidth();
        this.height            = this.bg.getRegionHeight();
        this.buttonRect.width  = this.bg.getRegionWidth();
        this.buttonRect.height = this.bg.getRegionHeight();
    }

    public void setSize(int _width, int _height)
    {
        this.width  = _width;
        this.height = _height;
    }

    public void update()
    {
        buttonTimer += Gdx.graphics.getDeltaTime();

        if (buttonTimer >= 0.04f)
        {
            if (isScaling)
            {
                if (direction == Movement._DIRECTION_UP)
                {
                    if ((scale += (scaleSpeed * direction)) > scaleMaximum)
                    {
                        scale = scaleMaximum;
                        direction *= -1;
                    }
                }
                else if (direction == Movement._DIRECTION_DOWN)
                {
                    if ((scale += (scaleSpeed * direction)) < scaleMinimum)
                    {
                        scale = scaleMinimum;
                        direction *= -1;
                    }
                }
                else
                {
                    direction = Movement._DIRECTION_DOWN;
                }
            }

            if (isRotating)
            {
                isClockwise = true;

                rotation = (rotation + rotateSpeed) % 360;
            }
            else if (isToAndFro)
            {
                if (rotateDist <= 0)
                {
                    rotateDist = (_INITIAL_DISTANCE * 2);
                    rotateDir *= -1;
                }
                else
                {
                    rotation = (rotation + (_ROTATE_SPEED * rotateDir)) % 360;

                    rotateDist -= _ROTATE_SPEED;
                }
            }

            buttonTimer = 0;
        }
    }

    @Override
    public void dispose()
    {
        bg         = null;
        bgPressed  = null;
        bgDisabled = null;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x
            + ", y: " + y
            + ", w: " + width
            + ", h: " + height
            + ", rotating: " + isRotating
            + ", scaling: " + isScaling
            + ", to and fro: " + isToAndFro;
    }
}
