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

package com.red7projects.dungeon.graphics.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.maths.Box;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.types.XYSetF;

public class ParallaxLayer implements Disposable
{
    public final  String        name;
    public final  TextureRegion textureRegion;
    private final Box           imageBox;
    public final  XYSetF        offset;

    public final XYSetF  position;
    public final boolean isActive;
    public       float   xSpeed;
    public       float   ySpeed;

    private final App app;

    ParallaxLayer(String textureName, App _app)
    {
        this.app = _app;

        this.name = textureName;

        Texture texture = app.assets.loadSingleAsset(textureName, Texture.class);

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textureRegion = new TextureRegion(texture);
        imageBox      = new Box(texture.getWidth(), texture.getHeight());
        offset        = new XYSetF(0f, texture.getHeight() - Gfx._VIEW_HEIGHT);
        position      = new XYSetF(0f, 0f);
        isActive      = true;
        xSpeed        = 0.0f;
        ySpeed        = 0.0f;

        setTextureRegion();
    }

    public void draw()
    {
        if (isActive)
        {
            app.spriteBatch.draw(textureRegion, position.getX(), position.getY());
        }
    }

    void scrollLayer(int xDirection, int yDirection)
    {
        if (isActive)
        {
            boolean isChanged = false;

            if (xDirection == Movement._DIRECTION_LEFT)
            {
                offset.addXWrapped(xSpeed, 0.0f, imageBox.width - Gfx._VIEW_WIDTH);
                isChanged = true;
            }
            else
            {
                if (xDirection == Movement._DIRECTION_RIGHT)
                {
                    offset.subXWrapped(xSpeed, 0.0f, imageBox.width - Gfx._VIEW_WIDTH);
                    isChanged = true;
                }
            }

            if (isChanged)
            {
                setTextureRegion();
            }
        }
    }

    void setTextureRegion()
    {
        textureRegion.setRegion
            (
                (int) offset.getX(),
                (int) offset.getY(),
                Gfx._VIEW_WIDTH,
                Gfx._VIEW_HEIGHT
            );
    }

    public void reset()
    {
        offset.set(0, 0);
        position.set(0, 0);
    }

    @Override
    public void dispose()
    {
        app.assets.unloadAsset(name);
        textureRegion.getTexture().dispose();
    }
}
