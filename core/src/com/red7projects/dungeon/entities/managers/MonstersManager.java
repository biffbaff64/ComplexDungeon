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
import com.red7projects.dungeon.assets.GfxAsset;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.Bouncer;
import com.red7projects.dungeon.entities.characters.Scorpion;
import com.red7projects.dungeon.entities.characters.StormDemon;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.utils.logging.Trace;

//@formatter:off
public class MonstersManager extends GenericEntityManager
{
    private final GfxAsset[] monsterTypes =
        {
            new GfxAsset
                (
                    GraphicID.G_STORM_DEMON,
                    Settings._STORM_DEMON,
                    GameAssets._STORM_DEMON_ASSET,
                    GameAssets._STORM_DEMON_FRAMES
                ),
            // -------------
            new GfxAsset
                (
                    GraphicID.G_BOUNCER,
                    Settings._BOUNCER,
                    GameAssets._BOUNCER_ASSET,
                    GameAssets._BOUNCER_FRAMES,
                    Animation.PlayMode.LOOP
                ),
            // -------------
            new GfxAsset
                (
                    GraphicID.G_SCORPION,
                    Settings._SCORPION,
                    GameAssets._SCORPION_ASSET,
                    GameAssets._SCORPION_FRAMES,
                    Animation.PlayMode.LOOP
                ),
            // -------------
        };

    private static final int _STORM_DEMON   = 0;
    private static final int _BOUNCER       = 1;
    private static final int _SCORPION      = 2;

    public MonstersManager(final App _app)
    {
        super(GraphicID._MONSTER, "", _app);
    }

    @Override
    public void init()
    {
        EntityStats.maxBouncers         = app.mapUtils.findMultiTiles(monsterTypes[_BOUNCER].graphicID).size;
        EntityStats.maxScorpions        = app.mapUtils.findMultiTiles(monsterTypes[_SCORPION].graphicID).size;

        EntityStats.minStormDemons = 1;
        EntityStats.minBouncers = 3;

        EntityStats.numStormDemons      = 0;
        EntityStats.numBouncers         = 0;
        EntityStats.numScorpions        = 0;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
        createStormDemons(monsterTypes[_STORM_DEMON]);
        createScorpions(monsterTypes[_SCORPION]);
        createBouncers(monsterTypes[_BOUNCER]);
    }

    private void createScorpions(GfxAsset gfxAsset)
    {
        Trace.__FILE_FUNC();

        if (app.settings.isEnabled(gfxAsset.preference))
        {
            graphicID = gfxAsset.graphicID;

            Array<MarkerTile> markerTiles = app.mapUtils.findMultiTiles(graphicID);

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

                entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                Scorpion scorpion = new Scorpion(graphicID, app);
                scorpion.initialise(entityDescriptor);
                app.entityData.addEntity(scorpion);

                EntityStats.log(graphicID);
            }
        }
    }

    private void createStormDemons(GfxAsset gfxAsset)
    {
        Trace.__FILE_FUNC();

        if (app.settings.isEnabled(gfxAsset.preference))
        {
            graphicID = gfxAsset.graphicID;

            Array<MarkerTile> markerTiles = app.mapUtils.findMultiTiles(graphicID);

            for (MarkerTile tile : markerTiles)
            {
                super.create
                    (
                        gfxAsset.asset,
                        gfxAsset.frames,
                        Animation.PlayMode.LOOP_PINGPONG,
                        tile._X,
                        tile._Y
                    );

                entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);
                entityDescriptor._ANIM_RATE   = 0.5f / 6f;

                StormDemon demon = new StormDemon(graphicID, app);
                demon.initialise(entityDescriptor);
                app.entityData.addEntity(demon);

                EntityStats.log(graphicID);
            }
        }
    }

    private void createBouncers(GfxAsset gfxAsset)
    {
        Trace.__FILE_FUNC();

        if (app.settings.isEnabled(gfxAsset.preference))
        {
            graphicID = gfxAsset.graphicID;

            Array<SimpleVec2> coordinates = findMultiCoordinates(graphicID);

            if (coordinates.size > 0)
            {
                int x = MathUtils.random((Gfx.getMapWidth() / Gfx.getTileWidth()) - 1);
                int y = MathUtils.random((Gfx.getMapHeight() / Gfx.getTileHeight()) - 1);

                if (app.mapUtils.isValidPosition(x, y, graphicID))
                {
                    super.create
                        (
                            gfxAsset.asset,
                            gfxAsset.frames,
                            Animation.PlayMode.LOOP,
                            x,
                            y
                        );

                    entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                    Bouncer bouncer = new Bouncer(graphicID, app);
                    bouncer.initialise(entityDescriptor);
                    app.entityData.addEntity(bouncer);

                    EntityStats.log(graphicID);
                }
            }
        }
    }
}
