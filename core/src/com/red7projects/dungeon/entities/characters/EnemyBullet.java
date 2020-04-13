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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.managers.ExplosionManager;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class EnemyBullet extends GdxSprite
{
    private static final float _SPEED_X = 6.0f;
    private static final float _SPEED_Y = 6.0f;

    private final App       app;
    private       Vector2   targetPosition;
    private       StopWatch lifeSpan;
    private       float     lifeTime;
    private       Vector2   launchPos;
    private       GdxSprite parent;

    public EnemyBullet(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_ENEMY_WEAPON;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_DOOR | Gfx.CAT_WALL;

        setCollisionListener();

        parent      = entityDescriptor._PARENT;
        stopWatch   = StopWatch.start();
        restingTime = 2000;
        lifeSpan    = StopWatch.start();
        isRotating  = true;
        rotateSpeed = -4.0f;

        if (entityDescriptor._PARENT.gid == GraphicID.G_TURRET)
        {
            sprite.setPosition
                (
                    parent.getPosition().x + (parent.frameWidth / 4),
                    parent.getPosition().y + (parent.frameHeight / 4)
                );

            targetPosition = new Vector2
                (
                (int) (getPosition().x - Gfx._VIEW_HALF_WIDTH) + MathUtils.random(Gfx._VIEW_WIDTH),
                (int) (getPosition().y - Gfx._VIEW_HALF_HEIGHT) + MathUtils.random(Gfx._VIEW_HEIGHT)
                );
            lifeTime = 2000;
        }
        else
        {
            sprite.setPosition
                (
                    parent.getPosition().x + 48,
                    parent.getPosition().y + 55
                );

            targetPosition = new Vector2(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY());
            lifeTime = 5000;
        }

        initXY.set(getPosition().x, getPosition().y);
        launchPos = new Vector2(sprite.getX(), sprite.getY());
        setAction(Actions._RUNNING);

        faceTarget();
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _RUNNING:
            {
                if (lifeSpan.time(TimeUnit.MILLISECONDS) > lifeTime)
                {
                    setAction(Actions._HURT);
                }
                else
                {
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

    private void faceTarget()
    {
        Vector2 vector2 = getTargetVector();

        calculateMove(vector2.nor());
    }

    private Vector2 getTargetVector()
    {
        Vector2 bulletPos = new Vector2(sprite.getX(), sprite.getY());

        return new Vector2(targetPosition.sub(bulletPos));
    }

    private void calculateMove(Vector2 vector2)
    {
        float xMove = (_SPEED_X * vector2.x);
        float yMove = (_SPEED_Y * vector2.y);

        speed.set(xMove, yMove);
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


