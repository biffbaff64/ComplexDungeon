/*
 *  Copyright 15/08/2018 Red7Projects.
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.Trace;

public class InfoBox extends GdxSprite
{
    public final  GraphicID parentID;
    private final App       app;

    public InfoBox(App _app)
    {
        super(GraphicID.G_MESSAGE_BUBBLE, _app);

        this.app = _app;
        this.parentID = GraphicID.G_NO_ID;
    }

    @Override
    public void initialise(EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_NOTHING;
        collisionObject.collidesWith = Gfx.CAT_NOTHING;

        setAction(Actions._STANDING);
        elapsedAnimTime = 0;
        isDrawable = true;
        collisionObject.action = Actions._INACTIVE;

        distance.set(0, 4);
        distanceReset.set(distance.getX(), distance.getY() * 2);
        speed.set(0, 0.2f);
        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                if (distance.getY() <= 0)
                {
                    direction.toggleY();
                    distance.set(distanceReset);
                }

                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                distance.subX(speed.getX());
                distance.subY(speed.getY());
            }
            break;

            case _DYING:
            {
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
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        if (!AppConfig.gamePaused
            && !app.getHud().messageManager.messageActive
            && (app.mainGameScreen.gameState.get() == StateID._STATE_GAME))
        {
            super.draw(spriteBatch);
        }
    }
}
