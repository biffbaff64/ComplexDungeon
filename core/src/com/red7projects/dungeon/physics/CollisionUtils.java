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
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.TileID;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.utils.logging.Trace;

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
     * Tidy ALL currently active collision objects by removing any _DEAD
     * objects from the object list.
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
     * Checks to see if the two specified {@link GdxSprite} objects are collidable.
     * The test is designed to see if 'entity' can collide with 'target'.
     *
     * @param entity The first object.
     * @param target The second object.
     *
     * @return TRUE if collidable, else FALSE.
     */
    public boolean canCollide(GdxSprite entity, GdxSprite target)
    {
        return ((entity.collisionObject.collidesWith & target.collisionObject.bodyCategory) != 0)
            && ((target.collisionObject.collidesWith & entity.collisionObject.bodyCategory) != 0)
            && (entity.collisionObject.index != target.collisionObject.index);
    }

    public boolean filter(short theEntityMask, short theContactMask)
    {
        return (((theEntityMask & theContactMask) != 0) && ((theContactMask & theEntityMask) != 0));
    }

    /**
     * Gets the {@link TileID} at the specified world coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     *
     * @return the tile ID.
     */
    public TileID getTileAtPosition(int x, int y)
    {
        TileID tileID = TileID._UNKNOWN;

        TiledMapTileLayer.Cell cell = app.mapData.markerTilesLayer.getCell(x, y);

        if (cell != null)
        {
            tileID = TileID.fromValue(cell.getTile().getId());
        }

        return tileID;
    }

    public CollisionObject getBoxHittingTop(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingTop);
    }

    public CollisionObject getBoxHittingBottom(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingBottom);
    }

    public CollisionObject getBoxHittingLeft(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingLeft);
    }

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
