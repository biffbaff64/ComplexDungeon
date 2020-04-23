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

package com.red7projects.dungeon.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.Soldier;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.utils.logging.Trace;

public class SoldierManager extends GenericEntityManager
{
    public SoldierManager(final App _app)
    {
        super(GraphicID.G_SOLDIER, "", _app);
    }

    @Override
    public void init()
    {
        EntityStats.maxSoldiers = app.mapUtils.findMultiTiles(GraphicID.G_SOLDIER).size;
        EntityStats.numSoldiers = 0;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
        Trace.__FILE_FUNC();

        if (Settings.isEnabled(Settings._SOLDIER))
        {
            graphicID = GraphicID.G_SOLDIER;

            Array<MarkerTile> markerTiles = app.mapUtils.findMultiTiles(GraphicID.G_SOLDIER);

            for (MarkerTile tile : markerTiles)
            {
                super.create
                    (
                        GameAssets._SOLDIER_IDLE_DOWN_ASSET,
                        GameAssets._SOLDIER_IDLE_FRAMES,
                        Animation.PlayMode.LOOP,
                        tile._X,
                        tile._Y
                    );

                entityDescriptor._LINK = tile._LINK;
                entityDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_SOLDIER);

                Soldier soldier = new Soldier(GraphicID.G_SOLDIER, app);
                soldier.initialise(entityDescriptor);
                app.entityData.addEntity(soldier);

                EntityStats.log(GraphicID.G_SOLDIER);
            }
        }
    }
}
