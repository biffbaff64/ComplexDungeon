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
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.Trace;

public class LoopBlock extends BaseEnemy
{
    private static final float _SPEED = 8.0f;

    private final App app;

    public LoopBlock(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        super.initialise(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON;

        setCollisionListener();

        setAction(Actions._STANDING);

        direction.set(entityDescriptor._DIR);

        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            speed.setX(_SPEED);
            isFlippedX = true;
        }
        else if (direction.getX() == Movement._DIRECTION_RIGHT)
        {
            speed.setX(_SPEED);
            isFlippedX = false;
        }
        else
        {
            speed.setX(0);
        }

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            speed.setY(_SPEED);
            isFlippedY = false;
        }
        else if (direction.getY() == Movement._DIRECTION_DOWN)
        {
            speed.setY(_SPEED);
            isFlippedY = true;
        }
        else
        {
            speed.setY(0);
        }

        isAnimating     = true;
        localIsDrawable = true;
        isDrawable      = true;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                setAction(Actions._RUNNING);
            }
            break;

            case _RUNNING:
            {
                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                if (sprite.getX() > Gfx.getMapWidth())
                {
                    sprite.translateX(-Gfx.getMapWidth());
                }
                else if ((sprite.getX() + frameWidth) < 0)
                {
                    sprite.translateX(Gfx.getMapWidth());
                }

                if (sprite.getY() > Gfx.getMapHeight())
                {
                    sprite.translateY(-Gfx.getMapHeight());
                }
                else if ((sprite.getY() + frameHeight) < 0)
                {
                    sprite.translateY(Gfx.getMapHeight());
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

    private void setCollisionListener()
    {
        addCollisionListener(new CollisionListener()
        {
            @Override
            public void onPositiveCollision(final GraphicID spriteHittingGid)
            {
                if (spriteHittingGid == GraphicID.G_PLAYER)
                {
                    app.getPlayer().sprite.translate
                        (
                            speed.getX() * direction.getX(),
                            speed.getY() * direction.getY()
                        );
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
}
