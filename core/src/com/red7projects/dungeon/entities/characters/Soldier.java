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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.systems.RoamingSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.DirectionAnim;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;

import java.util.concurrent.TimeUnit;

public class Soldier extends BaseEnemy
{
    private RoamingSystem roamingSystem;
    private Speed         previousSpeed;
    private App           app;

    public Soldier(final GraphicID _gid, final App _app)
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
                                    | Gfx.CAT_VILLAGER
                                    | Gfx.CAT_WALL
                                    | Gfx.CAT_DOOR
                                    | Gfx.CAT_ENEMY
                                    | Gfx.CAT_ENTITY_BARRIER
                                    | Gfx.CAT_WEAPON;

        roamingSystem                   = new RoamingSystem(app);
        roamingSystem.speedX            = 3.0f;
        roamingSystem.speedY            = 3.0f;

        stopWatch           = StopWatch.start();
        invisibilityTimer   = StopWatch.start();
        restingTime         = (5 + MathUtils.random(3)) * 1000;
        destination         = new Vector2();
        previousSpeed       = new Speed();

        distance.setEmpty();
        speed.setEmpty();
        direction.standStill();

        animation.setFrameDuration(0.5f / 6f);
        elapsedAnimTime = MathUtils.random(entityDescriptor._FRAMES);

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
                if (app.entityUtils.isOnScreen(this))
                {
                    setAction(Actions._RUNNING);
                }
            }
            break;

            case _RUNNING:
            {
                //
                // Aim for the player
                destination.set(app.getPlayer().getPosition().x, app.getPlayer().getPosition().y);

                //
                // Movement speed is set here
                roamingSystem.faceTarget(destination.x, destination.y, this);

                moveSoldier();
            }
            break;

            case _FIGHTING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    if (animation.isAnimationFinished(elapsedAnimTime))
                    {
                        setAction(Actions._STANDING);

                        stopWatch.reset();
                        restingTime = 1000;
                        isShooting  = false;
                        justBegunStanding = true;
                    }
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

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (localIsDrawable)
        {
            super.draw(spriteBatch);
        }

        if ((altAnim != null)
            && ((getSpriteAction() == Actions._SPAWNING)
            || (getSpriteAction() == Actions._TELEPORTING)))
        {
            spriteBatch.draw
                (
                    app.entityUtils.getKeyFrame(altAnim, elapsedAltAnimTime, false),
                    sprite.getX(),
                    sprite.getY()
                );
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX() + 24;
        collisionObject.rectangle.y      = sprite.getY() + 8;
        collisionObject.rectangle.width  = frameWidth - 48;
        collisionObject.rectangle.height = frameHeight - 16;

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge   = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    @Override
    public void positiveCollisionResponse(final GraphicID spriteHittingGid)
    {
    }

    private void moveSoldier()
    {
        previousSpeed.set(speed);

        setDirection();

        checkAnimationChange();

        sprite.translate(speed.getX(), speed.getY());
    }

    private void setDirection()
    {
        direction.standStill();

        if (Math.abs(speed.y) > Math.abs(speed.x))
        {
            if (speed.y > 0)
            {
                direction.setY(Movement._DIRECTION_UP);
            }
            else if (speed.y < 0)
            {
                direction.setY(Movement._DIRECTION_DOWN);
            }
        }
        else
        {
            if (speed.x > 0)
            {
                direction.setX(Movement._DIRECTION_RIGHT);
            }
            else if (speed.x < 0)
            {
                direction.setX(Movement._DIRECTION_LEFT);
            }
        }
    }

    private void checkAnimationChange()
    {
        final DirectionAnim[] runningAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._SOLDIER_RUN_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_RUN_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_RUN_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._SOLDIER_RUN_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_RUN_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_RUN_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._SOLDIER_RUN_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_RUN_DOWN_ASSET),
            };

        final DirectionAnim[] fightAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._SOLDIER_FIGHT_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_FIGHT_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_FIGHT_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._SOLDIER_FIGHT_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_FIGHT_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_FIGHT_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._SOLDIER_FIGHT_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_FIGHT_DOWN_ASSET),
            };

        final DirectionAnim[] idleAnims =
            {
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL, GameAssets._SOLDIER_IDLE_DOWN_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP, GameAssets._SOLDIER_IDLE_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_IDLE_DOWN_ASSET),

                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP, GameAssets._SOLDIER_IDLE_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_IDLE_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_IDLE_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP, GameAssets._SOLDIER_IDLE_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN, GameAssets._SOLDIER_IDLE_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._SOLDIER_IDLE_RIGHT_ASSET),
            };

        if (getSpriteAction() == Actions._FIGHTING)
        {
            EntityDescriptor descriptor = new EntityDescriptor();

            descriptor._FRAMES   = GameAssets._SOLDIER_FIGHT_FRAMES;
            descriptor._PLAYMODE = Animation.PlayMode.NORMAL;
            descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_SOLDIER_FIGHT);

            String asset = fightAnims[0].animation;

            for (DirectionAnim directionAnim : fightAnims)
            {
                if ((direction.getX() == directionAnim.dirX)
                    && (direction.getY() == directionAnim.dirY))
                {
                    asset = directionAnim.animation;
                }
            }

            descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

            setAnimation(descriptor, 1.0f);
        }
        else if (getSpriteAction() == Actions._RUNNING)
        {
            if (!speed.equals(previousSpeed))
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._SOLDIER_RUN_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_SOLDIER);

                String asset = runningAnims[0].animation;

                for (DirectionAnim directionAnim : runningAnims)
                {
                    if ((direction.getX() == directionAnim.dirX)
                        && (direction.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 0.5f);
            }
        }
        else if (getSpriteAction() == Actions._STANDING)
        {
            if (justBegunStanding)
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._SOLDIER_IDLE_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_SOLDIER);

                String asset = idleAnims[0].animation;

                for (DirectionAnim directionAnim : idleAnims)
                {
                    if ((direction.getX() == directionAnim.dirX)
                        && (direction.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 1.0f);

                justBegunStanding = false;
            }
        }
    }
}
