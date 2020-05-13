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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.GameListener;
import com.red7projects.dungeon.game.PointsManager;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Proximity;
import com.red7projects.dungeon.utils.logging.Trace;

public class TreasureChest extends GdxSprite
{
    private static final int _COINS_FROM_CHEST = 25;
    private static final float _DISTANCE = 16.0f;
    private static final float _SPEED    = 0.2f;

    private Proximity      proximity;
    private App            app;

    public TreasureChest(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_COLLECTIBLE;
        collisionObject.collidesWith = Gfx.CAT_PLAYER;

        proximity = new Proximity();
        isAnimating = false;

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setAction(Actions._STANDING);

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

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            case _INACTIVE:
            {
            }
            break;

            case _OPENING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    collect();

                    setAction(Actions._INACTIVE);
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
        TextureRegion region;

        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            region = app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false);
        }
        else
        {
            if (getSpriteAction() == Actions._INACTIVE)
            {
                region = animFrames[animFrames.length - 1];
            }
            else
            {
                region = animFrames[0];
            }
        }

        sprite.setRegion(region);
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX();
        collisionObject.rectangle.y      = sprite.getY();
        collisionObject.rectangle.width  = frameWidth;
        collisionObject.rectangle.height = frameHeight - 24;

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    private void collect()
    {
        if (gid == GraphicID.G_TREASURE_CHEST)
        {
            openTreasureChest();
            app.gameListener.stackPush(GameListener.Stack._SCORE, PointsManager.getPoints(gid));
        }
        else
        {
            if (gid == GraphicID.G_MYSTERY_CHEST)
            {
                openMysteryChest();
            }
        }
    }

    private void openTreasureChest()
    {
        for (int i=0; i<_COINS_FROM_CHEST; i++)
        {
            EntityDescriptor descriptor = new EntityDescriptor
                (
                    (int) this.sprite.getX() / Gfx.getTileWidth(),
                    (int) (this.sprite.getY() + this.frameHeight) / Gfx.getTileHeight(),
                    app.entityUtils.getInitialZPosition(GraphicID.G_COIN),
                    app.assets.getAnimationsAtlas().findRegion(GameAssets._COIN_ASSET),
                    GameAssets._COIN_FRAMES,
                    Animation.PlayMode.LOOP
                );


            descriptor._INDEX = app.entityData.entityMap.size;
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_COIN);

            Pickup pickup = new Pickup(GraphicID.G_COIN, app, true);
            pickup.initialise(descriptor);

            pickup.autoCollectDelay = 1000 + (i * 100);

            app.entityData.addEntity(pickup);
        }
    }

    private void openMysteryChest()
    {
        GraphicID pickupGid;

        if ((app.getRoomSystem().activeRoom.mysteryChestsAvailable == 1)
            && (app.gameProgress.keyCount.isEmpty()))
        {
            pickupGid = GraphicID.G_KEY;
        }
        else
        {

        }
    }
}
