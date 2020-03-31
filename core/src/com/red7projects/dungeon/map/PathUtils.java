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

package com.red7projects.dungeon.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.logging.Trace;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class PathUtils
{
    public Array<FixedPath> paths;
    public int numberOfPaths;

    private static final String _PATH_NUMBER = "Path Number";

    public PathUtils()
    {
        Trace.__FILE_FUNC();
    }

    public Array<FixedPath> getPaths()
    {
        return paths;
    }

    public boolean setup(final App app)
    {
        Trace.__FILE_FUNC();

        MapLayer mapLayer = app.mapData.currentMap.getLayers().get
            (
                app.mapData.mapLayerNames[MapData._PATHS_LAYER]
            );

        boolean hasPathData = false;

        if (mapLayer != null)
        {
            MapObjects mapObjects = mapLayer.getObjects();

            if (mapObjects != null)
            {
                numberOfPaths = establishNumberOfPaths(mapObjects);

                if (numberOfPaths > 0)
                {
                    paths = new Array<>();

                    for (int i = 0; i < numberOfPaths; i++)
                    {
                        paths.add(new FixedPath(i));
                    }

                    loadFromMap(mapObjects);

                    hasPathData = true;

                    Trace.dbg("Room " + app.getRoomSystem().getActiveRoomName() + " contains " + paths.size + " paths.");
                }
            }
            else
            {
                Trace.dbg("Room " + app.getRoomSystem().getActiveRoomName() + " contains a MapLayer but no MapObjects");
            }
        }
        else
        {
            Trace.dbg("Room " + app.getRoomSystem().getActiveRoomName() + " contains no path data.");
        }

        return hasPathData;
    }

    /**
     * Load the path data from the TiledMap.
     * <p>
     * Nodes are identified by a MarkerTile of nodeName.
     * Node properties will specify parent path and position in the path.
     *
     * @param mapObjects The TiledMap Object Layer
     */
    private void loadFromMap(final MapObjects mapObjects)
    {
        Trace.__FILE_FUNC();

        for (MapObject object : mapObjects)
        {
            if (object instanceof PolygonMapObject)
            {
                int pathNum = (int) object.getProperties().get(_PATH_NUMBER);

                Polygon polygon = ((PolygonMapObject) object).getPolygon();

                float[] points = polygon.getTransformedVertices();

                for (int i=0; i<points.length; i+=2)
                {
                    paths.get(pathNum).data.add(new Vector2(points[i], points[i + 1]));
                }
            }
        }
    }

    /**
     * Establish how many seperate paths there are.
     *
     * @param mapObjects The TiledMap Object Layer
     * @return The number of paths found.
     */
    private int establishNumberOfPaths(final MapObjects mapObjects)
    {
        int pathCount = -1;

        for (MapObject object : mapObjects)
        {
            if (object instanceof PolygonMapObject)
            {
                if (object.getProperties().containsKey(_PATH_NUMBER))
                {
                    if ((int) object.getProperties().get(_PATH_NUMBER) > pathCount)
                    {
                        pathCount++;
                    }
                }
            }
        }

        return pathCount + 1;
    }

    public Vector2 getVector(int pathNum, int index)
    {
        return paths.get(pathNum).data.get(index);
    }

    public int getRandomPath()
    {
        int randomPath;

        if (paths.size <= 1)
        {
            randomPath = 0;
        }
        else
        {
            randomPath = MathUtils.random(paths.size - 1);
        }

        return randomPath;
    }
}
