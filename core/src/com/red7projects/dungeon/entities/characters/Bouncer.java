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
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.physics.Movement;

import java.util.concurrent.TimeUnit;

public class Bouncer extends BaseEnemy
{
    private static final float _SPEEDX          = 2.0f;
    private static final float _SPEEDY          = 2.0f;
    private static final float _SPEEDX_RANDOM   = 2.0f;
    private static final float _SPEEDY_RANDOM   = 2.0f;

    private StopWatch stunnedTimer;
    private final App app;

    public Bouncer(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        super.initialise(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER
//                                    | Gfx.CAT_VILLAGER
//                                    | Gfx.CAT_ENEMY
                                    | Gfx.CAT_WALL
                                    | Gfx.CAT_DOOR
//                                    | Gfx.CAT_DECORATION
//                                    | Gfx.CAT_COLLECTIBLE
                                    | Gfx.CAT_ENTITY_BARRIER;

        animation.setFrameDuration(entityDescriptor._ANIM_RATE);

        direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN);
        setSpeed();

        isRotating      = true;
        rotateSpeed     = -4.0f;
        stunnedTimer    = StopWatch.start();
        stopWatch       = StopWatch.start();
        originalColor   = sprite.getColor();

        setCollisionListener();
        initSpawning();
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _SPAWNING:
            {
                updateSpawning();
            }
            break;

            case _STANDING:
            {
                setAction(Actions._RUNNING);

                stopWatch.reset();
                restingTime = 10000 + (MathUtils.random(5) * 5000);
            }
            break;

            case _RUNNING:
            case _STUNNED:
            {
                sprite.translate
                    (
                        speed.getX() * direction.getX(),
                        speed.getY() * direction.getY()
                    );

                if (getSpriteAction() == Actions._STUNNED)
                {
                    if (stunnedTimer.time(TimeUnit.MILLISECONDS) >= 150)
                    {
                        sprite.setColor(Color.WHITE);
                        sprite.setAlpha(1.0f);

                        setAction(Actions._STANDING);
                    }
                }
                else
                {
                    if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                    {
                        setAction(Actions._TELEPORTING);
                    }
                }
            }
            break;

            case _TELEPORTING:
            {
                if (sprite.getColor().a <= 0)
                {
                    reposition();
                }
                else
                {
                    sprite.setAlpha(sprite.getColor().a - 0.01f);
                }
            }
            break;

            case _HURT:
            {
                setAction(Actions._STANDING);
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

    /**
     * onPositiveCollision() and onNegativeCollision are
     * called BEFORE the main update method.
     * Collision related responses can be set here and handled
     * in the update() method.
     */
    private void setCollisionListener()
    {
        addCollisionListener(new CollisionListener()
        {
            @Override
            public void onPositiveCollision(final GraphicID spriteHittingGid)
            {
                if ((collisionObject.hasContactLeft() && (direction.getX() == Movement._DIRECTION_LEFT))
                    || (collisionObject.hasContactRight() && (direction.getX() == Movement._DIRECTION_RIGHT)))
                {
                    direction.toggleX();
                    stunnedTimer.reset();
                    sprite.setColor(Color.LIGHT_GRAY);
                    setAction(Actions._STUNNED);
                    setSpeed();
                }

                if ((collisionObject.hasContactUp() && (direction.getY() == Movement._DIRECTION_UP))
                    || (collisionObject.hasContactDown() && (direction.getY() == Movement._DIRECTION_DOWN)))
                {
                    direction.toggleY();
                    stunnedTimer.reset();
                    sprite.setColor(Color.LIGHT_GRAY);
                    setAction(Actions._STUNNED);
                    setSpeed();
                }
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

    private void setSpeed()
    {
        speed.set(MathUtils.random(_SPEEDX_RANDOM) + _SPEEDX, MathUtils.random(_SPEEDY_RANDOM) + _SPEEDY);
    }

    private void reposition()
    {
        int x = MathUtils.random((Gfx.getMapWidth() / Gfx.getTileWidth()) - 1);
        int y = MathUtils.random((Gfx.getMapHeight() / Gfx.getTileHeight()) - 1);

        if (app.mapUtils.isValidPosition(x, y, this.gid))
        {
            sprite.setPosition(x * Gfx.getTileWidth(), y * Gfx.getTileHeight());

            initSpawning();
        }
    }
}
