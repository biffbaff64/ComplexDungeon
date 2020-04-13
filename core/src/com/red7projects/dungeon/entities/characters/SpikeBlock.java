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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

import java.util.concurrent.TimeUnit;

public class SpikeBlock extends BaseEnemy
{
    private static final float _SPEED = 8.0f;

    private       Speed   fastSpeed;
    private       Speed   slowSpeed;
    private final App     app;

    public SpikeBlock(final GraphicID _gid, final App _app)
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

        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            speed.setX(_SPEED);
            isFlippedX = true;
            setHorizontalMovementBounds();
        }
        else if (direction.getX() == Movement._DIRECTION_RIGHT)
        {
            speed.setX(_SPEED);
            isFlippedX = false;
            setHorizontalMovementBounds();
        }
        else
        {
            speed.setX(0);
        }

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            speed.setY(_SPEED);
            isFlippedY = false;
            setVerticalMovementBounds();
        }
        else if (direction.getY() == Movement._DIRECTION_DOWN)
        {
            speed.setY(_SPEED);
            isFlippedY = true;
            setVerticalMovementBounds();
        }
        else
        {
            speed.setY(0);
        }

        animation.setFrameDuration(1.0f / 6f);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        slowSpeed = new Speed(speed);
        fastSpeed = new Speed(speed.x * 2, speed.y * 2);

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
                    speed.set(fastSpeed);
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
        if (getSpriteAction() == Actions._MOVING_IN)
        {
            setAction(Actions._STANDING);
        }
        else
        {
            setAction(Actions._MOVING_IN);
            speed.set(slowSpeed);
        }

        stopWatch.reset();
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
