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
import com.red7projects.dungeon.entities.characters.FlameThrower;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.Trace;

//@formatter:off
public class FlamesManager extends GenericEntityManager
{
    public FlamesManager(final App _app)
    {
        super(GraphicID.G_FLAME_THROWER, Preferences._FLAME_THROWER, _app);
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

        if (app.preferences.isEnabled((Preferences._FLAME_THROWER)))
        {
            graphicID = GraphicID.G_FLAME_THROWER;

            Array<MarkerTile> tiles = app.mapUtils.findMultiTiles(GraphicID.G_FLAME_THROWER);

            for (MarkerTile tile : tiles)
            {
                String asset;

                if (tile._DIR.getY() != Movement._DIRECTION_STILL)
                {
                    asset = GameAssets._FLAME_THROW_VERTICAL_ASSET;
                }
                else
                {
                    asset = GameAssets._FLAME_THROW_ASSET;
                }

                super.create
                    (
                        asset,
                        GameAssets._FLAME_THROW_FRAMES,
                        Animation.PlayMode.LOOP_PINGPONG,
                        tile._X,
                        tile._Y
                    );

                entityDescriptor._DIR.set(tile._DIR);
                entityDescriptor._DIST  = tile._DIST;
                entityDescriptor._SPEED = tile._SPEED;

                if (GameAssets._FLAME_THROW_VERTICAL_ASSET.equals(asset))
                {
                    entityDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_FLAME_THROWER_VERTICAL);
                }
                else
                {
                    entityDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_FLAME_THROWER);
                }

                FlameThrower flameThrower = new FlameThrower(GraphicID.G_FLAME_THROWER, app);
                flameThrower.initialise(entityDescriptor);

                app.entityData.addEntity(flameThrower);

                EntityStats.log(GraphicID.G_FLAME_THROWER);
            }
        }
    }
}
