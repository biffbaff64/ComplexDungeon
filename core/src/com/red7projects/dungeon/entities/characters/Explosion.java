/*
 *  Copyright 10/05/2018 Red7Projects.
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
import com.red7projects.dungeon.entities.hero.MainPlayer;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;

public class Explosion extends GdxSprite
{
    private       GdxSprite parent;
    private final App       app;

    /**
     * Constructor
     *
     * @param graphicID The {@link GraphicID of this sprite}
     * @param _app     an instance of the {@link App}
     */
    public Explosion(GraphicID graphicID, App _app)
    {
        super(graphicID, _app);

        this.app = _app;
    }

    @Override
    public void initialise(EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_NOTHING;
        collisionObject.collidesWith = Gfx.CAT_NOTHING;

        this.parent = entityDescriptor._PARENT;

        initXY.set(sprite.getX(), sprite.getY());

        animation.setFrameDuration(0.4f / 6.0f);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        sprite.setCenter(parent.sprite.getX() + (parent.frameWidth / 2), parent.sprite.getY() + (parent.frameHeight / 2));
        setAction(Actions._RUNNING);

        if (app.entityUtils.isOnScreen(this))
        {
            Sfx.inst().startSound(Sfx.inst().SFX_EXPLOSION_1);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        if (animation.isAnimationFinished(elapsedAnimTime))
        {
            setAction(Actions._DEAD);
            isDrawable = false;

            if (parent != null)
            {
                if (parent.gid == GraphicID.G_PLAYER)
                {
                    ((MainPlayer) parent).handleDying();
                }
                else
                {
                    parent.setAction(Actions._DYING);
                }
            }
        }
        else
        {
            if (animation.getKeyFrameIndex(elapsedAnimTime) == (animFrames.length / 4))
            {
                parent.isDrawable = false;
            }

            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }
}
