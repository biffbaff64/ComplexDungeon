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
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.deprecated.Beetle;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.assets.GfxAsset;

//@formatter:off
public class BeetleManager extends GenericEntityManager
{
    private final GfxAsset[] beetleTypes =
        {
            new GfxAsset
                (
                    GraphicID.G_BEETLE,
                    Preferences._BEETLE,
                    GameAssets._GREEN_BEETLE_ASSET,
                    GameAssets._GREEN_BEETLE_FRAMES
                )
        };

    public BeetleManager(final App _app)
    {
        super(GraphicID._MONSTER, "", _app);
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
        createBeetles(beetleTypes[0]);
    }

    private void createBeetles(GfxAsset gfxAsset)
    {
        if (app.preferences.isEnabled(gfxAsset.preference))
        {
            graphicID = GraphicID.G_BEETLE;

            Array<MarkerTile> markerTiles = app.mapUtils.findMultiTiles(GraphicID.G_BEETLE);

            if (markerTiles.size > 0)
            {
                for (MarkerTile tile : markerTiles)
                {
                    super.create
                        (
                            gfxAsset.asset,
                            gfxAsset.frames,
                            Animation.PlayMode.LOOP,
                            tile._X,
                            tile._Y
                        );

                    entityDescriptor._LINK = tile._LINK;
                    entityDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_BEETLE);

                    Beetle beetle = new Beetle(GraphicID.G_BEETLE, app);
                    beetle.initialise(entityDescriptor);
                    app.entityData.addEntity(beetle);

                    EntityStats.log(GraphicID.G_BEETLE);
                }
            }
        }
    }
}
