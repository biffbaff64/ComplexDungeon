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

package com.red7projects.dungeon.entities.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.entities.objects.Linker;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.Box;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.utils.logging.Trace;

public class PowerBeam extends GdxSprite implements Linker
{
    private final Color[] colourList = new Color[]
        {
            Color.ORANGE,
            Color.WHITE,
            Color.YELLOW,
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
        };

    private int       colourIndex;
    private int       width;
    private int       height;
    private NinePatch image9;
    private Box       beamSize;
    private int       link;

    public PowerBeam(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        beamSize = new Box();
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        width = Gfx.getTileWidth() * beamSize.width;
        height = Gfx.getTileHeight() * beamSize.height;

        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        setAction(Actions._STANDING);

        SimpleVec2F pos = new SimpleVec2F(sprite.getX(), sprite.getY());

        if (width > height)
        {
            pos.x += (float) (Gfx.getTileWidth() / 4);
            width -= (float) (Gfx.getTileWidth() / 4);
        }
        else
        {
            pos.y += (float) (Gfx.getTileHeight() / 4);
            height -= (float) (Gfx.getTileHeight() / 4);
        }

        sprite.setPosition(pos.x, pos.y);

        setCollisionListener();

        colourIndex = MathUtils.random(colourList.length - 1);

        image9 = new NinePatch(entityDescriptor._ASSET, 1, 1, 1, 1);
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                image9.setColor(colourList[colourIndex]);

                if (++colourIndex >= colourList.length)
                {
                    colourIndex = 0;
                }
            }
            break;

            case _HIDING:
            {
                setAction(Actions._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        updateCommon();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isDrawable)
        {
            image9.draw(spriteBatch, sprite.getX(), sprite.getY(), width, height);
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX();
        collisionObject.rectangle.y = sprite.getY();
        collisionObject.rectangle.width = this.width;
        collisionObject.rectangle.height = this.height;

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    @Override
    public void setLink(int _link)
    {
        this.link = _link;
    }

    @Override
    public int getLink()
    {
        return link;
    }

    @Override
    public void action()
    {
    }

    public void setBeamSize(Box _box)
    {
        beamSize.set(_box);
    }

    private void setCollisionListener()
    {
        addCollisionListener(new CollisionListener()
        {
            @Override
            public void onPositiveCollision(final GraphicID spriteHittingGid)
            {
            }

            @Override
            public void onNegativeCollision()
            {
            }

            @Override
            public void dispose()
            {
            }
        });
    }
}
