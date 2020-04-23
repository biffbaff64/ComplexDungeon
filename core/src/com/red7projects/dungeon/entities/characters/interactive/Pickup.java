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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.EmptyCollisionListener;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.GameListener;
import com.red7projects.dungeon.game.PointsManager;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class Pickup extends GdxSprite
{
    private static final byte _KEYS             = 0;
    private static final byte _COIN             = 1;
    private static final byte _GEM              = 2;
    private static final byte _SHIELD           = 3;
    private static final byte _LITTER           = 4;
    private static final byte _BOOK             = 5;
    private static final byte _RUNE             = 6;
    private static final byte _ACTIVATOR_COIN   = 7;

    private final byte[] pickupValues =
        {
            1,      // ONE Key
            1,      // ONE Coin
            1,      // ONE Gem
            25,     // 25 Health points
            1,      // ONE Litter
            1,      // ONE Book
            1,      // ONE Rune
            20,     // 20 Coins activated
        };

    public float autoCollectDelay;

    private StopWatch autoCollectTimer;
    private boolean   canAutoCollect;

    private final App app;

    public Pickup(final GraphicID _gid, final App _app, final boolean _canAutoCollect)
    {
        super(_gid, _app);

        this.app            = _app;
        this.canAutoCollect = _canAutoCollect;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_COLLECTIBLE;
        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY;

        isAnimating = (entityDescriptor._FRAMES > 1);

        if (!isAnimating || (gid == GraphicID.G_KEY))
        {
            isRotating  = true;
            rotateSpeed = -(0.2f + MathUtils.random(0.3f));
        }
        else
        {
            animation.setFrameDuration(entityDescriptor._ANIM_RATE);

            int limit = MathUtils.random(5);

            for (int i = 0; i < limit; i++)
            {
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
            }
        }

        setAction(Actions._STANDING);
        setScale();

        addCollisionListener(new EmptyCollisionListener());

        if (canAutoCollect)
        {
            autoCollectTimer = StopWatch.start();
        }
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                if (isOkToCollect())
                {
                    if (canAutoCollect)
                    {
                        if (autoCollectTimer.time(TimeUnit.MILLISECONDS) > autoCollectDelay)
                        {
                            setHiding();
                        }
                    }
                    else if (Intersector.overlaps(app.getPlayer().getCollisionRectangle(), getCollisionRectangle()))
                    {
                        setHiding();
                    }
                }
            }
            break;

            case _HIDING:
            {
                if (!app.mapData.viewportBox.contains(collisionObject.rectangle.x, collisionObject.rectangle.y))
                {
                    collect();

                    setAction(Actions._DYING);
                }
                else
                {
                    sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

                    distance.subX(speed.getX());
                    distance.subY(speed.getY());
                }
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
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    private void setScale()
    {
        switch (gid)
        {
            case G_KEY:
            {
                sprite.setScale(1.3f);
            }
            break;

            case G_SHIELD:
            case G_LITTER:
            case G_RUNE:
            case G_BOOK:
            default:
            {
            }
            break;
        }
    }

    private boolean isOkToCollect()
    {
        boolean canCollect;

        switch (gid)
        {
            case G_SHIELD:
            {
                canCollect = !app.getHud().getHealthBar().isFull();
            }
            break;

            case G_KEY:
            {
                canCollect = !app.gameProgress.keyCount.isFull();
            }
            break;

            case G_LITTER:
            case G_RUNE:
            case G_BOOK:
            default:
            {
                canCollect = true;
            }
            break;
        }

        return canCollect;
    }

    private void collect()
    {
        switch (gid)
        {
            case G_COIN:
            {
                app.gameListener.stackPush(GameListener.Stack._COIN, pickupValues[_COIN]);
            }
            break;

            case G_GEM:
            {
                app.gameListener.stackPush(GameListener.Stack._GEM, pickupValues[_GEM]);
            }
            break;

            case G_KEY:
            {
                app.gameListener.stackPush(GameListener.Stack._KEY, pickupValues[_KEYS]);

                app.getHud().messageManager.addZoomMessage
                    (
                        GameAssets._KEY_COLLECTED_MSG_ASSET,
                        2000,
                        552,
                        (Gfx._VIEW_HEIGHT - 270)
                    );
            }
            break;

            case G_SHIELD:
            {
                app.gameListener.stackPush(GameListener.Stack._SHIELD, pickupValues[_SHIELD]);
            }
            break;

            case G_LITTER:
            case G_RUNE:
            case G_BOOK:
            default:
                break;
        }

        app.gameListener.stackPush(GameListener.Stack._SCORE, PointsManager.getPoints(gid));
    }

    private void setHiding()
    {
        speed.set(12, 12);

        setTarget();
        setAction(Actions._HIDING);

        isRotating = false;
    }

    private void setTarget()
    {
        distance.set(Gfx._VIEW_WIDTH, Gfx._VIEW_HEIGHT);
        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
    }
}
