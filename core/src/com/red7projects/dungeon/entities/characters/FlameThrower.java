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
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class FlameThrower extends GdxSprite
{
    private boolean isVertical;
    private final App app;

    public FlameThrower(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        if (entityDescriptor._DIR.getX() != Movement._DIRECTION_STILL)
        {
            if (isFlippedX = (entityDescriptor._DIR.getX() == Movement._DIRECTION_LEFT))
            {
                sprite.translateX(animFrames[0].getRegionWidth() * Movement._DIRECTION_LEFT);
            }

            sprite.translateY(128 * Movement._DIRECTION_DOWN);

            isVertical = false;
        }

        if (entityDescriptor._DIR.getY() != Movement._DIRECTION_STILL)
        {
            isFlippedY = (entityDescriptor._DIR.getY() == Movement._DIRECTION_UP);

            if (!isFlippedY)
            {
                sprite.translateY(animFrames[0].getRegionHeight() * Movement._DIRECTION_DOWN);
            }

            sprite.translateX(128 * Movement._DIRECTION_LEFT);

            isVertical = true;
        }

        animation.setFrameDuration(0.8f / 6.0f);
        setAction(Actions._STANDING);

        setCollisionListener();

        stopWatch = StopWatch.start();
        restingTime     = 5000 + (MathUtils.random(10) * 100);
        canFlip         = true;
        isDrawable      = false;
        isAnimating     = false;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(Actions._RUNNING);
                    isDrawable = true;
                    isAnimating = true;
                    stopWatch.reset();
                }
            }

            case _RUNNING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime * 2)
                {
                    setAction(Actions._STANDING);
                    isDrawable = false;
                    isAnimating = false;
                    stopWatch.reset();
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
    public void updateCollisionBox()
    {
        if (isVertical)
        {
            collisionObject.rectangle.x = sprite.getX() + (frameWidth / 3);

            if (isFlippedY)
            {
                collisionObject.rectangle.y = sprite.getY();
            }
            else
            {
                collisionObject.rectangle.y = sprite.getY() + (frameHeight / 6);
            }

            collisionObject.rectangle.width  = frameWidth / 3;
            collisionObject.rectangle.height = frameHeight - (frameHeight / 6);
        }
        else
        {
            collisionObject.rectangle.x      = sprite.getX();
            collisionObject.rectangle.y      = sprite.getY();
            collisionObject.rectangle.width  = frameWidth;
            collisionObject.rectangle.height = frameHeight;
        }

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge = collisionObject.rectangle.y + collisionObject.rectangle.height;
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
