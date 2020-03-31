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

import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.physics.Movement;

import java.util.concurrent.TimeUnit;

public class SpikeBall extends BaseEnemy
{
    public SpikeBall(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);
    }

    @Override
    public void initialise(final EntityDescriptor descriptor)
    {
        super.initialise(descriptor);

        collisionObject.bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON | Gfx.CAT_DECORATION;

        setCollisionListener();

        isRotating = true;
        rotateSpeed = 6.0f;
        localIsDrawable = true;

        direction.set(descriptor._DIR);
        distance.set(descriptor._DIST.x * Gfx.getTileWidth(), descriptor._DIST.y * Gfx.getTileHeight());
        distanceReset.set(distance);

        speed.set(descriptor._SPEED);
        setAction(Actions._STANDING);
        stopWatch = StopWatch.start();

        if (descriptor._DIR.getX() != Movement._DIRECTION_STILL)
        {
            setHorizontalMovementBounds();

            speed.x = Math.max(2.0f + MathUtils.random(2.0f), speed.x);
        }
        else if (descriptor._DIR.getY() != Movement._DIRECTION_STILL)
        {
            setVerticalMovementBounds();

            speed.y = Math.max(2.0f + MathUtils.random(2.0f), speed.y);
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

            case _PAUSED:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(Actions._STANDING);
                }
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

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        animate();

        updateCommon();
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
