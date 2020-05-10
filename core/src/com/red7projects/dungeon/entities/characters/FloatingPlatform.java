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

import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class FloatingPlatform extends BaseEnemy
{
    public FloatingPlatform(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        final EntityDescriptor descriptor = new EntityDescriptor(entityDescriptor);

        super.initialise(descriptor);

        collisionObject.bodyCategory = Gfx.CAT_PLATFORM;
        collisionObject.collidesWith = Gfx.CAT_PLAYER;

        setCollisionListener();

        direction.set(descriptor._DIR);
        distance.set
            (
                descriptor._DIST.x * Gfx.getTileWidth(),
                descriptor._DIST.y * Gfx.getTileHeight()
            );
        distanceReset.set(distance);

        setAction(Actions._STANDING);
        localIsDrawable = true;
        stopWatch = StopWatch.start();
        restingTime = 2000;

        if (descriptor._DIR.getX() != Movement._DIRECTION_STILL)
        {
            setHorizontalMovementBounds();

            speed.set(6.0f, 0);
        }
        else if (descriptor._DIR.getY() != Movement._DIRECTION_STILL)
        {
            setVerticalMovementBounds();

            speed.set(0, 6.0f);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                setAction(Actions._RUNNING);
            }
            break;

            case _RUNNING:
            {
                checkMovementBounds();

                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                distance.subXMinZero(speed.getX());
                distance.subYMinZero(speed.getY());
            }
            break;

            case _PAUSED:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(Actions._RUNNING);
                }
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
    public void onMovementBoundsTurn(int edgeSide)
    {
        setAction(Actions._PAUSED);
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
