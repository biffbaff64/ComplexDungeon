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

package com.red7projects.dungeon.entities.characters.deprecated;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.entities.systems.TargettingSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

public class Beetle extends BaseEnemy
{
    private       TargettingSystem targettingSystem;
    private       GdxSprite        targetSprite;
    private final App              app;

    public Beetle(final GraphicID _graphicID, final App _app)
    {
        super(_graphicID, _app);

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

        animation.setFrameDuration(entityDescriptor._ANIM_RATE);

        destination       = new Vector2();
        targetSprite      = app.entityUtils.getRandomSprite(this);
        stopWatch         = StopWatch.start();
        invisibilityTimer = StopWatch.start();
        restingTime       = (2 + MathUtils.random(3)) * 1000;

        float scale;
        int   random = MathUtils.random(100);

        float speedTemp;

        if (random > 98)
        {
            scale     = 0.5f;
            strength  = 1;
            speedTemp = 0.5f;
        }
        else if (random > 80)
        {
            scale     = 0.75f;
            strength  = 2;
            speedTemp = 0.75f;
        }
        else
        {
            scale     = 1.0f;
            strength  = 3;
            speedTemp = 1.0f;
        }

        targettingSystem                 = new TargettingSystem(app);
        targettingSystem.speedX          = speedTemp;
        targettingSystem.speedY          = speedTemp;
        targettingSystem.rotationAllowed = true;

        sprite.setScale(scale);

        setCollisionListener();

        initSpawning();

        localIsDrawable = true;
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
                targettingSystem.update(this, targetSprite);

                setAction(Actions._RUNNING);
            }
            break;

            case _RUNNING:
            {
                targettingSystem.update(this, targetSprite);

                sprite.translate(speed.getX(), speed.getY());
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

        animate();

        updateCommon();
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
                    this.sprite.getX(),
                    this.sprite.getY()
                );
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
                invisibilityTimer.reset();

                targetSprite = app.entityUtils.getRandomSprite(collisionObject.parentSprite);

                collisionObject.action = Actions._INVISIBLE;
                setAction(Actions._RUNNING);
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
