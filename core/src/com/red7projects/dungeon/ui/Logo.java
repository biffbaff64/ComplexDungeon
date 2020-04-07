/*
 *  Copyright 24/04/2018 Red7Projects.
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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings("FieldCanBeLocal")
public class Logo extends Actor implements Disposable
{
    private final float _MAXIMUM_SCALE = 1.0f;
    private final float _MINIMUM_SCALE = 0.7f;

    public TextureRegion textureRegion;

    private float      delay;
    private float      logoScale;
    private float      logoSpeed;
    private float      logoTimer;
    private int        logoDirection;
    private SimpleVec2 logoPosition;
    private float      minimumScale;
    private float      maximumScale;

    public Logo(TextureRegion _textureRegion)
    {
        logoScale = 0.0f;
        logoSpeed = 0.04f;
        logoDirection = Movement._DIRECTION_UP;
        logoTimer = 0;
        logoPosition = new SimpleVec2();

        textureRegion = _textureRegion;

        minimumScale = _MINIMUM_SCALE;
        maximumScale = _MAXIMUM_SCALE;

        delay = 0;
    }

    public void update()
    {
        logoTimer += Gdx.graphics.getDeltaTime();

        if (logoTimer >= delay)
        {
            if (logoDirection == Movement._DIRECTION_UP)
            {
                if ((logoScale += (logoSpeed * logoDirection)) > maximumScale)
                {
                    logoScale = maximumScale;
                    logoDirection *= -1;
                    delay = 0.05f;
                }
            }
            else if (logoDirection == Movement._DIRECTION_DOWN)
            {
                if ((logoScale += (logoSpeed * logoDirection)) < minimumScale)
                {
                    logoScale = minimumScale;
                    logoDirection *= -1;
                }
            }

            logoTimer = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw
            (
                textureRegion,
                logoPosition.getX(), logoPosition.getY(),
                textureRegion.getRegionWidth() / 2f, textureRegion.getRegionHeight() / 2f,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                logoScale,
                logoScale,
                0.0f
            );
    }

    public void setPosition(int x, int y)
    {
        logoPosition.set(x, y);
    }

    public void setScaleSpeed(float speed)
    {
        this.logoSpeed = speed;
    }

    public void setScaleSize(float scale)
    {
        this.logoScale = scale;
    }

    public void setScaleLimits(float minScale, float maxScale)
    {
        this.minimumScale = minScale;
        this.maximumScale = maxScale;
    }

    public void setScaleDirection(int direction)
    {
        this.logoDirection = direction;
    }

    @Override
    public void dispose()
    {
        logoPosition = null;
        textureRegion = null;
    }
}
