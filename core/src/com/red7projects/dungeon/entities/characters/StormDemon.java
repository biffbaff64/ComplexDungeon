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

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.systems.EnemyAttackSystem;
import com.red7projects.dungeon.entities.systems.TargettingSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.types.XYSetF;

public class StormDemon extends BaseEnemy
{
    private static final float _SCALE_SPEED     = 0.01f;
    private static final float _SCALE_DISTANCE  = 0.75f;
    private static final float _MINIMUM_SCALE   = 0.9f;

    private Direction pulseDirection;
    private XYSetF pulseDistance;
    private TargettingSystem targettingSystem;
    private boolean          hasWarningShown;
    private final App app;

    public StormDemon(final GraphicID _gid, final App _app)
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
            | Gfx.CAT_WALL
            | Gfx.CAT_DOOR
            | Gfx.CAT_ENEMY
            | Gfx.CAT_WEAPON;

        animation.setFrameDuration(entityDescriptor._ANIM_RATE);

        destination = new Vector2();

        if (app.getPlayer() != null)
        {
            destination = new Vector2(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY());
        }

        targettingSystem  = new TargettingSystem(app);
        attackSystem      = new EnemyAttackSystem(this, app);
        stopWatch         = StopWatch.start();
        invisibilityTimer = StopWatch.start();
        restingTime       = (2 + MathUtils.random(3)) * 1000;
        isRotating        = true;
        rotateSpeed       = -1.0f;
        localIsDrawable   = true;
        hasWarningShown   = false;

        float speedTemp = 2f + MathUtils.random(2f);

        targettingSystem.speedX = speedTemp;
        targettingSystem.speedY = speedTemp;

        pulseDirection = new Direction(Movement._DIRECTION_OUT, Movement._DIRECTION_OUT);
        pulseDistance = new XYSetF
            (
                (sprite.getScaleX() - _MINIMUM_SCALE) + _SCALE_DISTANCE,
                (sprite.getScaleY() - _MINIMUM_SCALE) + _SCALE_DISTANCE
            );

        setCollisionListener();
        setAction(Actions._STANDING);
    }

    @Override
    public void preUpdate()
    {
        super.preUpdate();

        if (app.entityUtils.isOnScreen(this) && !hasWarningShown)
        {
            app.getHud().messageManager.addZoomMessage
                (
                    GameAssets._STORM_DEMON_WARNING_ASSET,
                    5000,
                    (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._STORM_DEMON_WARNING).getX()) / 2,
                    100
                );

            hasWarningShown = true;
        }
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                targettingSystem.update(this, app.getPlayer());

                if (targettingSystem.isAdjustingTarget
                    || Intersector.overlaps(app.getPlayer().viewBox.getRectangle(), collisionObject.rectangle))
                {
                    setAction(Actions._RUNNING);
                }
            }
            break;

            case _RUNNING:
            {
                if (!Intersector.overlaps(app.getPlayer().viewBox.getRectangle(), collisionObject.rectangle))
                {
                    setAction(Actions._STANDING);
                }
                else
                {
                    targettingSystem.update(this, app.getPlayer());

                    sprite.translate(speed.getX(), speed.getY());
                }
            }
            break;

            case _HURT:
            {
                restingTime = (2 + MathUtils.random(5)) * 1000;
                setAction(Actions._STANDING);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        pulse();
        animate();

        updateCommon();
    }

    private void pulse()
    {
        if (pulseDistance.isEmpty())
        {
            pulseDirection.toggle();
            pulseDistance.set(_SCALE_DISTANCE, _SCALE_DISTANCE);
        }
        else
        {
            sprite.setScale
                (
                    (sprite.getScaleX() + (_SCALE_SPEED * pulseDirection.getX())),
                    (sprite.getScaleY() + (_SCALE_SPEED * pulseDirection.getY()))
                );

            pulseDistance.subYMinZero(_SCALE_SPEED);
            pulseDistance.subXMinZero(_SCALE_SPEED);
        }
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
                if (!targettingSystem.isAdjustingTarget)
                {
                    targettingSystem.isAdjustingTarget = true;
                    invisibilityTimer.reset();
                    targettingSystem.setAdjustedTarget(collisionObject.parentSprite);

                    collisionObject.action = Actions._INVISIBLE;
                    setAction(Actions._RUNNING);
                }
            }

            @Override
            public void onNegativeCollision()
            {
                collisionObject.action = Actions._COLLIDABLE;
            }

            @Override
            public void dispose()
            {
            }
        });
    }
}
