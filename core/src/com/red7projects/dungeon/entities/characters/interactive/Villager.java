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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.graphics.SimpleDrawable;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Proximity;
import com.red7projects.dungeon.physics.Speed;

public class Villager extends GdxSprite
{
    private       EntityDescriptor descriptor;
    private       Proximity        proximity;
    private       boolean          canShowMessage;
    private final App              app;

    public Villager(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        descriptor = new EntityDescriptor(entityDescriptor);

        create(entityDescriptor);

        proximity = new Proximity();

        collisionObject.bodyCategory = Gfx.CAT_VILLAGER;
        collisionObject.collidesWith = Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_PLAYER;

        setAction(Actions._STANDING);

        sprite.setScale
            (
                app.getPlayer().sprite.getScaleX() + 0.1f,
                app.getPlayer().sprite.getScaleY() + 0.1f
            );

        canFlip = false;

        canShowMessage = false;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                canShowMessage = proximity.isVeryClose(this, app.getPlayer());

                if (canShowMessage)
                {
                    if (app.getPlayer().getSpriteAction() == Actions._STANDING)
                    {
                        app.mainGameScreen.gameState.set(StateID._STATE_MESSAGE_PANEL);

//                        if (!app.getHud().messageManager.doesPanelExist(GameAssets._MESSAGE_PANEL_ASSET))
//                        {
//                            app.getHud().messageManager.enable();
//                            app.getHud().messageManager.addSlidePanel(GameAssets._MESSAGE_PANEL_ASSET);
//                            app.getHud().messageManager.getCurrentPanel().set
//                                (
//                                    new SimpleVec2F(280, -400),
//                                    new SimpleVec2F(0, 400),
//                                    new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
//                                    new Speed(0, 40)
//                                );
//                        }
                    }
//                    else
//                    {
//                        if (app.getHud().messageManager.doesPanelExist(GameAssets._MESSAGE_PANEL_ASSET))
//                        {
//                            app.getHud().messageManager.closeSlidePanel();
//                        }
//                    }
                }

                if (app.getPlayer().topEdge < sprite.getY())
                {
                    descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(GameAssets._VILLAGER_IDLE_DOWN_ASSET);

                    setAnimation(descriptor, 1.0f);
                }
                else if (app.getPlayer().topEdge > (sprite.getY() + frameHeight))
                {
                    descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(GameAssets._VILLAGER_IDLE_UP_ASSET);

                    setAnimation(descriptor, 1.0f);
                }
                else
                {
                    if (app.getPlayer().sprite.getX() < sprite.getX())
                    {
                        descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(GameAssets._VILLAGER_IDLE_LEFT_ASSET);

                        setAnimation(descriptor, 1.0f);
                    }
                    else
                    {
                        descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(GameAssets._VILLAGER_IDLE_RIGHT_ASSET);

                        setAnimation(descriptor, 1.0f);
                    }
                }

            }
            break;

            case _TALKING:
            {
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
        collisionObject.rectangle.x      = sprite.getX() + 24;
        collisionObject.rectangle.y      = sprite.getY() + 8;
        collisionObject.rectangle.width  = frameWidth - 48;
        collisionObject.rectangle.height = frameHeight - 16;

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge   = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }
}
