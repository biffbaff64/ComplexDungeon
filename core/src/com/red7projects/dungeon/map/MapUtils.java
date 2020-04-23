/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 */

package com.red7projects.dungeon.map;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;

public class MapUtils
{
    private final App app;

    public MapUtils(App _app)
    {
        this.app = _app;
    }

    public void update() {}

    public void positionAt(int x, int y)
    {
        app.mapData.previousMapPosition.set(app.mapData.mapPosition);

        app.mapData.mapPosition.setY((int) Math.max(app.mapData.minScrollY, (y + (app.getPlayer().frameHeight / 2)) - (float)(Gfx._VIEW_HEIGHT / 2)));
        app.mapData.mapPosition.setY(Math.min(app.mapData.mapPosition.getY(), app.mapData.maxScrollY));

        app.mapData.mapPosition.setX((int) Math.max(app.mapData.minScrollX, (x + (app.getPlayer().frameWidth / 2)) - (float)(Gfx._VIEW_WIDTH / 2)));
        app.mapData.mapPosition.setX(Math.min(app.mapData.mapPosition.getX(), app.mapData.maxScrollX));
    }

    /**
     * Find all instances of Marker tiles with the specified GraphicID.
     *
     * @param targetGID The {@link GraphicID} to search for.
     *
     * @return  Array of valid {@link MarkerTile}.
     */
    public Array<MarkerTile> findMultiTiles(final GraphicID targetGID)
    {
        Array<MarkerTile> tiles = new Array<>();

        for (MarkerTile marker : app.gameUtils.getMapCreator().placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }

    public boolean isValidPosition(float x, float y, GraphicID _gid)
    {
        boolean isValid = true;

        Rectangle entityRect = new Rectangle
            (
                x * Gfx.getTileWidth(),
                y * Gfx.getTileHeight(),
                GameAssets.getAssetSize(_gid).getX(),
                GameAssets.getAssetSize(_gid).getY()
            );

        for(Rectangle rectangle : app.mapData.enemyFreeZones)
        {
            if (Intersector.overlaps(entityRect, rectangle))
            {
                isValid = false;
            }
        }

        if (isValid)
        {
            for (CollisionObject object : AABBData.boxes())
            {
                if (Intersector.overlaps(entityRect, object.rectangle))
                {
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
