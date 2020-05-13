/*
 *  Copyright 08/05/2018 Red7Projects.
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
import com.red7projects.dungeon.assets.GfxAsset;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.characters.PowerBeam;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.utils.logging.Trace;

public class PowerBeamManager extends GenericEntityManager
{
    private final GfxAsset[] beamTypes =
        {
            new GfxAsset
                (
                    GraphicID.G_LASER_BEAM_HORIZONTAL,
                    Settings._DEFAULT_ON,
                    GameAssets._LASER_BEAM_HORIZONTAL_ASSET
                ),
            new GfxAsset
                (
                    GraphicID.G_LASER_BEAM_VERTICAL,
                    Settings._DEFAULT_ON,
                    GameAssets._LASER_BEAM_VERTICAL_ASSET
                ),
        };

    public PowerBeamManager(App _app)
    {
        super(GraphicID.G_LASER_BEAM, Settings._DEFAULT_ON, _app);
    }

    @Override
    public void create()
    {
        Trace.__FILE_FUNC();

        for (GfxAsset gfxAsset : beamTypes)
        {
            graphicID = gfxAsset.graphicID;

            Array<MarkerTile> tiles = app.mapUtils.findMultiTiles(graphicID);

            for (MarkerTile tile : tiles)
            {
                super.create
                    (
                        gfxAsset.asset,
                        GameAssets._LASER_BEAM_FRAMES,
                        Animation.PlayMode.NORMAL,
                        tile._X,
                        tile._Y
                    );

                entityDescriptor._LINK = tile._LINK;

                PowerBeam powerBeam = new PowerBeam(graphicID, app);
                powerBeam.setBeamSize(tile._BOX);
                powerBeam.setLink(tile._LINK);
                powerBeam.initialise(entityDescriptor);

                app.entityData.addEntity(powerBeam);

                activeCount++;
            }
        }
    }
}
