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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.Entities;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.interactive.Pickup;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.utils.logging.Trace;

//@formatter:off
public class PickupManager extends GenericEntityManager
{
    private static final String[] gems =
        {
            GameAssets._BLUE_GEMS_ASSET,
            GameAssets._GREEN_GEMS_ASSET,
            GameAssets._GOLD_GEMS_ASSET,
            GameAssets._RED_GEMS_ASSET,
        };

    public PickupManager(final App _app)
    {
        super(GraphicID._PICKUP, "", _app);
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
        Trace.__FILE_FUNC();

        if (app.settings.isEnabled(Settings._PICKUPS))
        {
            for (EntityDef entityDef : Entities.entityList)
            {
                if (entityDef.type.equals(GraphicID._PICKUP))
                {
                    graphicID = entityDef.graphicID;

                    Array<MarkerTile> markerTiles = app.mapUtils.findMultiTiles(graphicID);

                    if (markerTiles.size > 0)
                    {
                        for (MarkerTile tile : markerTiles)
                        {
                            String asset;

                            if (graphicID == GraphicID.G_GEM)
                            {
                                asset = gems[MathUtils.random(gems.length - 1)];
                            }
                            else
                            {
                                asset = entityDef.asset;
                            }

                            super.create
                                (
                                    asset,
                                    entityDef.frames,
                                    (entityDef.frames > 1) ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL,
                                    tile._X,
                                    tile._Y
                                );

                            entityDescriptor.debug();

                            entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                            GdxSprite pickup = new Pickup(graphicID, app, false);
                            pickup.initialise(entityDescriptor);

                            app.entityData.addEntity(pickup);

                            EntityStats.log(graphicID);
                        }
                    }
                }
            }
        }
    }
}
