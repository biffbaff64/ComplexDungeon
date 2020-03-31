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

package com.red7projects.dungeon.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.managers.ExplosionManager;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.logging.Trace;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class PlayerBullet extends GdxSprite
{
    private static final float _SPEED_X = 32.0f;
    private static final float _SPEED_Y = 32.0f;

    private final App       app;
    private       Vector2   targetPosition;
    private       Vector2   launchPosition;
    private       StopWatch lifeSpan;
    private       float     lifeTime;
    private       GdxSprite parent;

    public PlayerBullet(final App _app)
    {
        super(GraphicID.G_ARROW, _app);

        this.app = _app;
    }

    @Override
    public void initialise(EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collisionObject.collidesWith = Gfx.CAT_ENEMY | Gfx.CAT_DOOR | Gfx.CAT_WALL;

        setCollisionListener();

        parent = entityDescriptor._PARENT;

        float x = parent.sprite.getX();
        float y = parent.sprite.getY();

        sprite.setPosition(x, y);
        direction.set(parent.lookingAt);

        initXY.set(sprite.getX(), sprite.getY());

        launchPosition = new Vector2(initXY.x, initXY.y);
        targetPosition = new Vector2
            (
                initXY.x + (Gfx._VIEW_HALF_WIDTH * direction.getX()),
                initXY.y + (Gfx._VIEW_HALF_HEIGHT * direction.getY())
            );
        setAction(Actions._RUNNING);

        Vector2 vector2 = getTargetPosition();

        faceTarget(vector2.x, vector2.y);

        isRotating      = false;
        stopWatch       = StopWatch.start();
        restingTime     = 2000;
        lifeSpan        = StopWatch.start();
        lifeTime        = 5000;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _RUNNING:
            {
                if ((lifeSpan.time(TimeUnit.MILLISECONDS) > lifeTime)
                    || !app.entityUtils.isOnScreen(this))
                {
                    setAction(Actions._HURT);
                }
                else
                {
                    Vector2 vector2 = getTargetPosition();

                    faceTarget(vector2.x, vector2.y);

                    sprite.translate(speed.getX(), speed.getY());
                }
            }
            break;

            case _HURT:
            case _KILLED:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION32, this, app);

                collisionObject.action = Actions._INVISIBLE;
                setAction(Actions._DEAD);
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
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    private void faceTarget(float targetX, float targetY)
    {
        Vector2 vector2 = getTargetVector(targetX, targetY);

        if (!isRotating)
        {
            sprite.setRotation(vector2.angle() - 90);
        }

        if (getSpriteAction() == Actions._RUNNING)
        {
            calculateMove(vector2.nor());
        }
    }

    @Contract(pure = true)
    private Vector2 getTargetPosition()
    {
        return targetPosition;
    }

    @NotNull
    private Vector2 getTargetVector(float targetX, float targetY)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 bulletPos = new Vector2(sprite.getX(), sprite.getY());

        return new Vector2(targetPos.sub(bulletPos));
    }

    private void calculateMove(@NotNull Vector2 vector2)
    {
        float xMove = (_SPEED_X * vector2.x);
        float yMove = (_SPEED_Y * vector2.y);

        speed.set(xMove, yMove);

        if (speed.isEmpty())
        {
            setAction(Actions._HURT);
        }
    }

    private void setCollisionListener()
    {
        addCollisionListener(new CollisionListener()
        {
            @Override
            public void onPositiveCollision(final GraphicID spriteHittingGid)
            {
                setAction(Actions._HURT);
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


