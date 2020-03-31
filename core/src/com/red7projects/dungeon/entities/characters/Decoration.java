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
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;

/**
 * Class for objects such as:-
 * -   Barrels
 * -   Vases
 * -   background fires
 */
public class Decoration extends GdxSprite implements CollisionListener
{
    private       boolean isMoveable;
    private final App     app;

    public Decoration(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        if (collisionObject != null)
        {
            collisionObject.bodyCategory = Gfx.CAT_DECORATION;
            collisionObject.collidesWith = Gfx.CAT_PLAYER
                                        | Gfx.CAT_ENEMY
                                        | Gfx.CAT_WEAPON
                                        | Gfx.CAT_OBSTACLE
                                        | Gfx.CAT_ENTITY_BARRIER
                                        | Gfx.CAT_DOOR;
        }

        if (isAnimating = (entityDescriptor._FRAMES > 1))
        {
            animation.setFrameDuration(entityDescriptor._ANIM_RATE);
        }

        isMoveable = ((gid == GraphicID.G_BARREL) || (gid == GraphicID.G_CRATE) || (gid == GraphicID.G_POT));

        addCollisionListener(this);
    }

    @Override
    public void update(final int spriteNum)
    {
        animate();

        if (this.isMoveable)
        {
            if (!distance.isEmpty())
            {
                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
                distance.sub(speed.getX(), speed.getY(), 0);
            }
        }

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
    public void draw(final SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    /**
     * Create collision object for this entity.
     * Alcove torches
     */
    @Override
    public void createCollisionObject()
    {
        if (this.gid != GraphicID.G_ALCOVE_TORCH)
        {
            super.createCollisionObject();
        }
    }

    @Override
    public void onPositiveCollision(GraphicID spriteHittingGid)
    {
        if (spriteHittingGid == GraphicID.G_PLAYER)
        {
            if (distance.isEmpty())
            {
                distance.set(Gfx.getTileWidth() / 2f, Gfx.getTileHeight() / 2f);
                speed.set(4.0f, 4.0f);

                setBounceDirection(spriteHittingGid);
            }
        }
        else
        {
            if (this.isMoveable)
            {
                distance.set(4.0f, 4.0f);
                speed.set(4.0f, 4.0f);

                setBounceDirection(spriteHittingGid);
            }
        }
    }

    private void setBounceDirection(GraphicID _gidHitting)
    {
        if (collisionObject.idTop == _gidHitting)
        {
            direction.setY(Movement._DIRECTION_DOWN);
        }
        else if (collisionObject.idBottom == _gidHitting)
        {
            direction.setY(Movement._DIRECTION_UP);
        }
        else
        {
            direction.setY(Movement._DIRECTION_STILL);
            distance.setY(0);
            speed.setY(0);
        }

        if (collisionObject.idLeft == _gidHitting)
        {
            direction.setX(Movement._DIRECTION_RIGHT);
        }
        else if (collisionObject.idRight == _gidHitting)
        {
            direction.setX(Movement._DIRECTION_LEFT);
        }
        else
        {
            direction.setX(Movement._DIRECTION_STILL);
            distance.setX(0);
            speed.setX(0);
        }
    }

    @Override
    public void onNegativeCollision()
    {
        if (distance.isEmpty())
        {
            direction.standStill();
            speed.setEmpty();
        }
    }
}
