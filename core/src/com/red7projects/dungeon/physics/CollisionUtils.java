/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.physics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.map.MarkerID;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.game.Actions;

@SuppressWarnings("JavaDoc")
public class CollisionUtils implements Disposable
{
    private final App app;

    public CollisionUtils(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public CollisionObject newObject()
    {
        return new CollisionObject();
    }

    public CollisionObject newObject(Rectangle rectangle)
    {
        return new CollisionObject(rectangle);
    }

    /**
     * Get a new object collision object.
     *
     * @param x         the x
     * @param y         the y
     * @param width     the width
     * @param height    the height
     * @param graphicID the object TYPE (_OBSTACLE or _ENTITY)
     *
     * @return the collision object
     */
    public CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        return new CollisionObject(x, y, width, height, graphicID);
    }

    /**
     * Tidy ALL currently active collision objects.
     */
    public void tidy()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            if (AABBData.boxes().get(i).action == Actions._DEAD)
            {
                AABBData.boxes().get(i).removeObjectFromList();
            }
        }
    }

    /**
     * Tidy (Kill) ALL currently active collision objects.
     */
    private void tidyAll()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            AABBData.boxes().get(i).action = Actions._DEAD;
        }

        tidy();
    }

    /**
     * Dump debug information for all
     */
    public void debugAll()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            AABBData.boxes().get(i).debug();
        }
    }

    /**
     * @param entity the entity
     * @param target the target
     *
     * @return the boolean
     */
    public boolean canCollide(GdxSprite entity, GdxSprite target)
    {
        return ((entity.collisionObject.collidesWith & target.collisionObject.bodyCategory) != 0)
            && ((target.collisionObject.collidesWith & entity.collisionObject.bodyCategory) != 0)
            && (entity.collisionObject.index != target.collisionObject.index);
    }

    /**
     * @param theEntityMask
     * @param theContactMask
     *
     * @return
     */
    public boolean filter(short theEntityMask, short theContactMask)
    {
        return (((theEntityMask & theContactMask) != 0) && ((theContactMask & theEntityMask) != 0));
    }

    /**
     * Gets marker tile on.
     *
     * @param x the x
     * @param y the y
     *
     * @return the marker tile on
     */
    public MarkerID getMarkerTileOn(int x, int y)
    {
        MarkerID markerID = MarkerID._UNKNOWN;

        TiledMapTileLayer.Cell cell = app.mapData.markerTilesLayer.getCell(x, y);

        if (cell != null)
        {
            markerID = MarkerID.fromValue(cell.getTile().getId());
        }

        return markerID;
    }

    /**
     *
     *
     * @param spriteObj the sprite obj
     *
     * @return
     */
    public int tileBelowX(GdxSprite spriteObj)
    {
        return (int) ((spriteObj.getCollisionRectangle().getX() + (Gfx.getTileWidth() / 2)) / Gfx.getTileWidth());
    }

    /**
     *
     *
     * @param spriteObj the sprite obj
     *
     * @return
     */
    public int tileBelowY(GdxSprite spriteObj)
    {
        int y;

        y = (int) ((spriteObj.sprite.getY() - (spriteObj.sprite.getY() % Gfx.getTileHeight())) / Gfx.getTileHeight());

        if ((spriteObj.sprite.getY() % Gfx.getTileHeight()) == 0)
        {
            y--;
        }

        return y;
    }

    /**
     * Gets box hitting top.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting top
     */
    public CollisionObject getBoxHittingTop(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingTop);
    }

    /**
     * Gets box hitting bottom.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting bottom
     */
    public CollisionObject getBoxHittingBottom(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingBottom);
    }

    /**
     * Gets box hitting left.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting left
     */
    public CollisionObject getBoxHittingLeft(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingLeft);
    }

    /**
     * Gets box hitting right.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting right
     */
    public CollisionObject getBoxHittingRight(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingRight);
    }

    @Override
    public void dispose()
    {
        tidyAll();
    }
}
