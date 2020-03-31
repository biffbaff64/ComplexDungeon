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
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.Turret;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;

public class TurretManager extends GenericEntityManager
{
    public TurretManager(final App _app)
    {
        super(GraphicID.G_TURRET, "", _app);
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
        if (app.preferences.isEnabled(Preferences._TURRETS))
        {
            graphicID = GraphicID.G_TURRET;

            Array<MarkerTile> tiles = app.mapUtils.findMultiTiles(GraphicID.G_TURRET);

            for (MarkerTile tile : tiles)
            {
                super.create
                    (
                        GameAssets._TURRET_ASSET,
                        GameAssets._TURRET_FRAMES,
                        Animation.PlayMode.NORMAL,
                        tile._X,
                        tile._Y
                    );

                entityDescriptor._LINK      = tile._LINK;
                entityDescriptor._SIZE      = GameAssets.getAssetSize(GraphicID.G_TURRET);
                entityDescriptor._ANIM_RATE = 1.0f;

                Turret turret = new Turret(GraphicID.G_TURRET, app);
                turret.initialise(entityDescriptor);

                app.entityData.addEntity(turret);

                EntityStats.log(GraphicID.G_TURRET);
            }
        }
    }
}
