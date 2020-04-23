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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class PathFollower extends BaseEnemy
{
    private       float     speedX;
    private       float     speedY;
    private       int       pathNumber;
    private       int       pathIndex;
    private       Direction pathDirection;
    private       int       maxPathIndex;
    private final App       app;

    public PathFollower(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        super.initialise(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_WALL | Gfx.CAT_DOOR | Gfx.CAT_ENEMY | Gfx.CAT_WEAPON;

        setCollisionListener();

        setAction(Actions._STANDING);

        final float speedTemp = 3.0f;

        speedX          = speedTemp;
        speedY          = speedTemp;
        stopWatch       = StopWatch.start();
        restingTime     = 1000 + (MathUtils.random(5) * 100);
        destination     = new Vector2();
        pathNumber      = MathUtils.random(app.pathUtils.getPaths().size - 1);
        maxPathIndex    = app.pathUtils.getPaths().get(pathNumber).data.size;
        pathIndex       = MathUtils.random(1, maxPathIndex - 1);
        pathDirection   = new Direction(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);

        sprite.setPosition
            (
                app.pathUtils.getPaths().get(pathNumber).data.get(pathIndex - 1).x,
                app.pathUtils.getPaths().get(pathNumber).data.get(pathIndex - 1).y
            );

        distance.setEmpty();
        speed.setEmpty();
        direction.standStill();

        localIsDrawable = true;

//        if (this.gid == GraphicID.G_FIRE_BALL)
//        {
//            sprite.setOrigin(52, 110);
//
//            canFlip = false;
//        }
//        else
//        {
//            if (this.gid == GraphicID.G_RED_MINE)
//            {
//                attackSystem    = new EnemyAttackSystem(this, app);
//                stopWatch       = StopWatch.start();
//                restingTime     = (2 + MathUtils.random(5)) * 1000;
//            }
//        }

        isRotating  = false;
        rotateSpeed = 0.0f;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                destination.set
                    (
                        app.pathUtils.getPaths().get(pathNumber).data.get(pathIndex).x,
                        app.pathUtils.getPaths().get(pathNumber).data.get(pathIndex).y
                    );

                setAction(Actions._RUNNING);
            }
            break;

            case _RUNNING:
            {
                faceTarget(destination.x, destination.y);

                moveWalker();

                if (collisionObject.rectangle.contains(destination.x + 8, destination.y + 8))
                {
                    pathIndex = (pathIndex + pathDirection.getX()) % maxPathIndex;

                    if (MathUtils.randomBoolean(0.25f))
                    {
                        stopWatch.reset();
                        restingTime = 1000 + (MathUtils.random(5) * 100);

                        setAction(Actions._PAUSED);
                    }
                    else
                    {
                        setAction(Actions._STANDING);
                    }
                }
//                else
//                {
//                    if (this.gid == GraphicID.G_RED_MINE)
//                    {
//                        if ((collisionObject.action != Actions._COLLIDING)
//                            && (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
//                            && (MathUtils.random(100) < 20))
//                        {
//                            attackSystem.shoot();
//
//                            isShooting = true;
//                            stopWatch.reset();
//                        }
//                    }
//                }
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

    private void faceTarget(float targetX, float targetY)
    {
        Vector2 vector2 = getTargetVector(targetX, targetY);

//        if (gid == GraphicID.G_FIRE_BALL)
//        {
//            sprite.setRotation(vector2.angle() - 180);
//        }

        if (getSpriteAction() == Actions._RUNNING)
        {
            calculateMove(vector2.nor());
        }
    }

    private Vector2 getTargetVector(float targetX, float targetY)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 walkerPos = new Vector2(sprite.getX(), sprite.getY());

        return new Vector2(targetPos.sub(walkerPos));
    }

    private void calculateMove(Vector2 vector2)
    {
        float xMove = (speedX * vector2.x);
        float yMove = (speedY * vector2.y);

        checkXMovement(xMove);
        checkYMovement(yMove);

        speed.set(xMove, yMove);
    }

    private void checkXMovement(float _xMove)
    {
        if (_xMove > 0)
        {
            direction.setX(Movement._DIRECTION_RIGHT);
        }
        else if (_xMove < 0)
        {
            direction.setX(Movement._DIRECTION_LEFT);
        }
        else
        {
            direction.setX(Movement._DIRECTION_STILL);
        }
    }

    private void checkYMovement(float _yMove)
    {
        if (_yMove > 0)
        {
            direction.setX(Movement._DIRECTION_UP);
        }
        else if (_yMove < 0)
        {
            direction.setX(Movement._DIRECTION_DOWN);
        }
        else
        {
            direction.setX(Movement._DIRECTION_STILL);
        }
    }

    private void moveWalker()
    {
        sprite.translate(speed.getX(), speed.getY());
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
