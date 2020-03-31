/*
 *  Copyright 02/05/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;

@SuppressWarnings("WeakerAccess")
public class CollisionHandler implements CollisionListener, Disposable
{
    public boolean isImpeded;

    private final App app;

    public CollisionHandler(App _app)
    {
        this.app = _app;
    }

    /**
     * Called when Collision is occurring.
     *
     * @param spriteHittingGid The GraphicdID of the entity
     *                  in collision with.
     */
    @Override
    public void onPositiveCollision(GraphicID spriteHittingGid)
    {
        switch (spriteHittingGid)
        {
            case _WALL:
            case G_DOOR:
            case G_OPEN_DOOR:
            case G_LOCKED_DOOR:
            case G_VILLAGER:
            {
                app.getPlayer().isOnPlatform = false;
                app.getPlayer().platformSprite = null;

                isImpeded = true;
            }
            break;

            case G_FLOATING_PLATFORM:
            {
                if (app.getPlayer().collisionObject.contactSprite.getCollisionRectangle().contains
                    (
                        (app.getPlayer().getPosition().x + (app.getPlayer().frameWidth / 2)),
                        app.getPlayer().getPosition().y
                    ))
                {
                    app.getPlayer().isOnPlatform = true;
                    app.getPlayer().platformSprite = app.getPlayer().collisionObject.contactSprite;
                }
            }
            break;

            // Objects that can be collided with, and
            // which WILL hurt LJM.
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_LASER_BEAM_HORIZONTAL:
            case G_LASER_BEAM_VERTICAL:
            case G_SPIKE_BALL:
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_DOUBLE_SPIKE_BLOCK:
            case G_JELLY_MONSTER:
            case G_ENEMY_BULLET:
            case G_FLAME_THROWER:
            case G_FLAME_THROWER_VERTICAL:
            case G_SOLDIER:
            {
                if ((app.getPlayer().getSpriteAction() != Actions._HURT)
                    && (app.getPlayer().getSpriteAction() != Actions._DYING))
                {
                    if (!Developer.isGodMode())
                    {
                        app.getPlayer().hurt(spriteHittingGid);
                    }

                    rebound();
                }

                app.getPlayer().isOnPlatform = false;
                app.getPlayer().platformSprite = null;
            }
            break;

            case G_NO_ID:
            default:
            {
                app.getPlayer().isOnPlatform = false;
                app.getPlayer().platformSprite = null;
            }
            break;
        }

        app.getPlayer().collisionObject.action = Actions._INVISIBLE;
    }

    /**
     * Called when there is no collision occurring.
     */
    @Override
    public void onNegativeCollision()
    {
        isImpeded = false;

        if (app.getPlayer().getSpriteAction() == Actions._HURT)
        {
            app.getPlayer().isHurting = false;
        }

        app.getPlayer().collisionObject.action = Actions._COLLIDABLE;
    }

    public int isNextTo(GraphicID graphicID)
    {
        int nextTo = 0;

        for (CollisionObject object : AABBData.boxes())
        {
            if (object.gid == graphicID)
            {
                if (object.rectangle.overlaps(app.getPlayer().sprite.getBoundingRectangle()))
                {
                    nextTo = object.index;
                }
            }
        }

        return nextTo;
    }

    public boolean isBlockedTop()
    {
        return validate(app.collisionUtils.getBoxHittingTop(app.getPlayer()).gid);
    }

    public boolean isBlockedBottom()
    {
        return validate(app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid);
    }

    public boolean isBlockedLeft()
    {
        return validate(app.collisionUtils.getBoxHittingLeft(app.getPlayer()).gid);
    }

    public boolean isBlockedRight()
    {
        return validate(app.collisionUtils.getBoxHittingRight(app.getPlayer()).gid);
    }

    private boolean validate(final GraphicID graphicID)
    {
        boolean isContact;

        switch (graphicID)
        {
            case _WALL:
            case G_LOCKED_DOOR:

            case G_VILLAGER:

            case G_POT:
            case G_BARREL:
            case G_TREASURE_CHEST:
            case G_MYSTERY_CHEST:
            case G_CRATE:

            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_LASER_BEAM:
            case G_SPIKE_BALL:
            case G_JELLY_MONSTER:
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_DOUBLE_SPIKE_BLOCK:
            case G_ENEMY_BULLET:
            case G_SOLDIER:
            {
                isContact = true;
            }
            break;

            case G_QUESTION_MARK:
            default:
            {
                isContact = false;
            }
            break;
        }

        return isContact;
    }

    private void rebound()
    {
        if (app.getPlayer().collisionObject.hasContactUp() && !app.getPlayer().collisionObject.hasContactDown())
        {
            app.getPlayer().sprite.translateY(4 * Movement._DIRECTION_DOWN);
        }
        else if (app.getPlayer().collisionObject.hasContactDown() && !app.getPlayer().collisionObject.hasContactUp())
        {
            app.getPlayer().sprite.translateY(4 * Movement._DIRECTION_UP);
        }

        if (app.getPlayer().collisionObject.hasContactLeft() && !app.getPlayer().collisionObject.hasContactRight())
        {
            app.getPlayer().sprite.translateX(4 * Movement._DIRECTION_RIGHT);
        }
        else if (app.getPlayer().collisionObject.hasContactRight() && !app.getPlayer().collisionObject.hasContactLeft())
        {
            app.getPlayer().sprite.translateX(4 * Movement._DIRECTION_LEFT);
        }
    }

    @Override
    public void dispose()
    {
    }
}
