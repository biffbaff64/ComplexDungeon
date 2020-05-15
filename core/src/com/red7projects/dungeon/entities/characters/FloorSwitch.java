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
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;

public class FloorSwitch extends GdxSprite
{
    private App app;

    public FloorSwitch(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_INTERACTIVE;
        collisionObject.collidesWith = Gfx.CAT_PLAYER;

        animation.setFrameDuration(entityDescriptor._ANIM_RATE);
        isAnimating = false;

        setAction(Actions._STANDING);
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            case _CLOSED:
            {
                if (gid == GraphicID.G_FLOOR_BUTTON)
                {
                    isAnimating = !app.gameProgress.keyCount.isEmpty();
                }
            }
            break;

            case _RUNNING:
            {
                if (app.entityData.entityMap.get(getLink()) instanceof PowerBeam)
                {
                    app.entityData.entityMap.get(getLink()).setAction(Actions._HIDING);
                    this.setAction(Actions._CLOSED);
                    isAnimating = false;
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
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX() - 8;
        collisionObject.rectangle.y = sprite.getY() - 8;
        collisionObject.rectangle.width = frameWidth + 16;
        collisionObject.rectangle.height = frameHeight + 16;

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }
}
