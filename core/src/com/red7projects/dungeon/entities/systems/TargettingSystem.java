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

package com.red7projects.dungeon.entities.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.physics.Movement;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TargettingSystem implements DecisionSystem
{
    public float   speedX;
    public float   speedY;
    public boolean isAdjustingTarget;
    public boolean rotationAllowed;
    public boolean checkMoveAllowed;

    private Vector2 destination;
    private App app;

    public TargettingSystem(App _app)
    {
        this.app = _app;

        speedX = 0;
        speedY = 0;

        destination = new Vector2();
        rotationAllowed = false;
        checkMoveAllowed = true;
    }

    @Override
    public void update(GdxSprite parentSprite, GdxSprite target)
    {
        Vector2 vector2 = getTargetPosition(parentSprite, target);

        faceTarget(vector2.x, vector2.y, parentSprite);
    }

    @Override
    public Vector2 getTargetPosition(GdxSprite parentSprite, GdxSprite target)
    {
        float targetX;
        float targetY;

        if (isAdjustingTarget)
        {
            targetX = destination.x;
            targetY = destination.y;

            if (parentSprite.invisibilityTimer.time(TimeUnit.MILLISECONDS) > 3500)
            {
                isAdjustingTarget = false;
            }
        }
        else
        {
            targetX = (target.sprite.getX() + (target.frameWidth / 2));
            targetY = (target.sprite.getY() + (target.frameHeight / 2));
        }

        return new Vector2(targetX, targetY);
    }

    @Override
    public void setAdjustedTarget(@NotNull GdxSprite parentSprite)
    {
        int trys = 0;

        do
        {
            destination.set
                (
                    MathUtils.random(Gfx.getMapWidth()),
                    MathUtils.random(Gfx.getMapHeight())
                );

            trys++;
        }
        while ((trys < 1000) && (app.getPlayer().viewBox.contains(destination)));

        parentSprite.collisionObject.action = Actions._INVISIBLE;
        parentSprite.invisibilityTimer = StopWatch.start();
    }

    @Override
    public void faceTarget(float targetX, float targetY, GdxSprite parentSprite)
    {
        Vector2 vector2 = getTargetVector(targetX, targetY, parentSprite);

        if (rotationAllowed)
        {
            parentSprite.sprite.setRotation(vector2.angle() - 90);
        }

        if (parentSprite.getSpriteAction() == Actions._RUNNING)
        {
            calculateMove(vector2.nor(), parentSprite);
        }
    }

    @Override
    public Vector2 getTargetVector(float targetX, float targetY, GdxSprite parentSprite)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 parentPos = new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY());

        return new Vector2(targetPos.sub(parentPos));
    }

    @Override
    public void calculateMove(@NotNull Vector2 vector2, GdxSprite parentSprite)
    {
        float xMove = (speedX * vector2.x);
        float yMove = (speedY * vector2.y);

        if (checkMoveAllowed)
        {
            xMove = checkXMovement(xMove, parentSprite);
            yMove = checkYMovement(yMove, parentSprite);
        }

        parentSprite.speed.set(xMove, yMove);

        if (parentSprite.speed.isEmpty())
        {
            parentSprite.setAction(Actions._STANDING);
        }
    }

    @Override
    public float checkXMovement(float _xMove, GdxSprite parentSprite)
    {
        if (_xMove > 0)
        {
            if (parentSprite.collisionObject.hasContactRight() && parentSprite.collisionObject.isContactObstacle)
            {
                _xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_RIGHT);
            }
        }
        else if (_xMove < 0)
        {
            if (parentSprite.collisionObject.hasContactLeft() && parentSprite.collisionObject.isContactObstacle)
            {
                _xMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_LEFT);
            }
        }
        else
        {
            _xMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return _xMove;
    }

    @Override
    public float checkYMovement(float _yMove, GdxSprite parentSprite)
    {
        if (_yMove > 0)
        {
            if (parentSprite.collisionObject.hasContactUp() && parentSprite.collisionObject.isContactObstacle)
            {
                _yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_UP);
            }
        }
        else if (_yMove < 0)
        {
            if (parentSprite.collisionObject.hasContactDown() && parentSprite.collisionObject.isContactObstacle)
            {
                _yMove = 0;
                parentSprite.direction.setX(Movement._DIRECTION_STILL);
            }
            else
            {
                parentSprite.direction.setX(Movement._DIRECTION_DOWN);
            }
        }
        else
        {
            _yMove = 0;
            parentSprite.direction.setX(Movement._DIRECTION_STILL);
        }

        return _yMove;
    }

    @Override
    public float distanceRemaining(GdxSprite parentSprite, Vector2 destination)
    {
        return new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY()).dst(destination);
    }
}
