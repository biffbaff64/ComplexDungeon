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

package com.red7projects.dungeon.entities.characters.interactive;

import com.badlogic.gdx.Gdx;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.physics.Movement;

public class MessageBubble extends GdxSprite
{
    private final App app;

    public MessageBubble(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_INTERACTIVE;
        collisionObject.collidesWith = Gfx.CAT_NOTHING;

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
        distance.set(0, entityDescriptor._DIST.y / 2);
        distanceReset.set(0, entityDescriptor._DIST.y);
        speed.set(0, 0.4f);

        setAction(Actions._STANDING);
    }

    @Override
    public void update(final int spriteNum)
    {
        if (getSpriteAction() == Actions._STANDING)
        {
            if (distance.isEmpty())
            {
                direction.toggleY();
                distance.set(distanceReset);
            }

            sprite.translateX(speed.getX() * direction.getX());
            sprite.translateY(speed.getY() * direction.getY());

            distance.subX(speed.getX());
            distance.subY(speed.getY());
        }
        else
        {
            Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
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
}
