/*
 *  Copyright 31/08/2018 Red7Projects.
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

package com.red7projects.dungeon.game;

import com.red7projects.dungeon.graphics.GraphicID;

public class PointsManager
{
    static class Points
    {
        final GraphicID gid;
        final int       points;

        Points(GraphicID _gid, int _points)
        {
            this.gid = _gid;
            this.points = _points;
        }
    }

    private static final Points[] pointsTable =
        {
            new Points(GraphicID.G_STORM_DEMON,     500),
            new Points(GraphicID.G_BOUNCER,         100),
            new Points(GraphicID.G_COIN,            25),
            new Points(GraphicID.G_GEM,             25),
            new Points(GraphicID.G_KEY,             50),
            new Points(GraphicID.G_TREASURE_CHEST,  100),
        };

    public static int getPoints(GraphicID gid)
    {
        int score = 0;

        for (final Points entry : pointsTable)
        {
            if (gid.equals(entry.gid))
            {
                score = entry.points;

                break;
            }
        }

        return score;
    }
}
