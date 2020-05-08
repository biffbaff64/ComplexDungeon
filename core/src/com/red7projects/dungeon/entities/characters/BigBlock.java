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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class BigBlock extends BaseEnemy
{
    private static final float _SPEED = 4.0f;

    private TextureRegion coverImage;
    private SimpleVec2F coverPos;
    private final App     app;

    public BigBlock(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        super.initialise(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON;

        setCollisionListener();

        setAction(Actions._STANDING);
        localIsDrawable = true;

        direction.set(entityDescriptor._DIR);
        distance.set
            (
                entityDescriptor._DIST.x * Gfx.getTileWidth(),
                entityDescriptor._DIST.y * Gfx.getTileHeight()
            );
        distanceReset.set(distance);

        speed.set(0, 0);

        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            speed.setX(_SPEED);
            isFlippedX = true;
            setHorizontalMovementBounds();

            coverImage = app.assets.getObjectsAtlas().findRegion(GameAssets._BLOCK_COVER_LEFT);
            coverPos = new SimpleVec2F(initXY.getX(), initXY.getY());
        }
        else if (direction.getX() == Movement._DIRECTION_RIGHT)
        {
            speed.setX(_SPEED);
            isFlippedX = false;
            setHorizontalMovementBounds();

            coverImage = app.assets.getObjectsAtlas().findRegion(GameAssets._BLOCK_COVER_RIGHT);
            coverPos = new SimpleVec2F(initXY.getX(), initXY.getY());
        }

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            speed.setY(_SPEED);
            isFlippedY = false;
            setVerticalMovementBounds();

            coverImage = app.assets.getObjectsAtlas().findRegion(GameAssets._BLOCK_COVER_BOTTOM);
            coverPos = new SimpleVec2F(initXY.getX() - Gfx.getTileWidth(), initXY.getY());
        }
        else if (direction.getY() == Movement._DIRECTION_DOWN)
        {
            speed.setY(_SPEED);
            isFlippedY = true;
            setVerticalMovementBounds();

            coverImage = app.assets.getObjectsAtlas().findRegion(GameAssets._BLOCK_COVER_BOTTOM);
            coverPos = new SimpleVec2F(initXY.getX() - Gfx.getTileWidth(), initXY.getY());
        }

        stopWatch = StopWatch.start();
        restingTime = (100 + MathUtils.random(100)) * 10;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(Actions._MOVING_OUT);
                }
            }
            break;

            case _MOVING_IN:
            case _MOVING_OUT:
            {
                checkMovementBounds();

                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                distance.subXMinZero(speed.getX());
                distance.subYMinZero(speed.getY());
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    @Override
    public void onMovementBoundsTurn(int edgeSide)
    {
        setAction(Actions._STANDING);
        stopWatch.reset();
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);

        if (coverImage != null)
        {
            spriteBatch.draw(coverImage, coverPos.getX(), coverPos.getY());
        }
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
