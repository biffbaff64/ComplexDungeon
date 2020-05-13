/*
 *  Copyright 03/12/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.entities.components.EntityManagerComponent;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.maths.SimpleVec2;

@SuppressWarnings("WeakerAccess")
public class GenericEntityManager implements EntityManagerComponent, Disposable
{
    public       boolean          canPlace;
    public       String           preferenceID;
    public final App              app;
    public       EntityDescriptor entityDescriptor;
    public       int              activeCount;
    public       GraphicID        graphicID;

    private final GraphicID managerID;

    public GenericEntityManager(final App _app)
    {
        this.app = _app;

        this.graphicID    = GraphicID.G_NO_ID;
        this.managerID    = GraphicID.G_NO_ID;
        this.preferenceID = "";
    }

    public GenericEntityManager(final GraphicID _graphicID, final String _prefID, final App _app)
    {
        this.app = _app;

        this.graphicID    = _graphicID;
        this.managerID    = _graphicID;
        this.preferenceID = _prefID;
    }

    @Override
    public void init()
    {
        activeCount = 0;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
    }

    @Override
    public void create(final String _asset, final int _frames, final Animation.PlayMode _mode, final int x, final int y)
    {
        entityDescriptor = new EntityDescriptor
            (
                x,
                y,
                app.entityUtils.getInitialZPosition(graphicID),
                app.assets.getAnimationsAtlas().findRegion(_asset),
                _frames,
                _mode
            );

        entityDescriptor._INDEX = app.entityData.entityMap.size;
    }

    @Override
    public SimpleVec2 findCoordinates(final GraphicID targetGID)
    {
        SimpleVec2 coords = new SimpleVec2();

        for (MarkerTile marker : app.gameUtils.getMapCreator().placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.set(marker._X, marker._Y);
            }
        }

        return coords;
    }

    @Override
    public Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID)
    {
        Array<SimpleVec2> coords = new Array<>();

        for (MarkerTile marker : app.gameUtils.getMapCreator().placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.add(new SimpleVec2(marker._X, marker._Y));
            }
        }

        return coords;
    }

    @Override
    public void free()
    {
        activeCount = Math.max(0, activeCount - 1);
    }

    @Override
    public void reset()
    {
        activeCount = 0;
    }

    @Override
    public int getActiveCount()
    {
        return activeCount;
    }

    @Override
    public void setActiveCount(final int numActive)
    {
        activeCount = numActive;
    }

    @Override
    public GraphicID getGID()
    {
        return managerID;
    }

    @Override
    public boolean isPlaceable()
    {
        return canPlace;
    }

    @Override
    public void setPlaceable(final boolean placeable)
    {
        canPlace = placeable;
    }

    @Override
    public void dispose()
    {
        preferenceID = null;
        entityDescriptor = null;
    }
}
