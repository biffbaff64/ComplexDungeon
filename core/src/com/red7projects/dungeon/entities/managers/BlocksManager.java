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
import com.red7projects.dungeon.entities.characters.LoopBlock;
import com.red7projects.dungeon.entities.characters.SpikeBall;
import com.red7projects.dungeon.entities.characters.SpikeBlock;
import com.red7projects.dungeon.entities.characters.interactive.FloatingPlatform;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.utils.logging.Trace;

public class BlocksManager extends GenericEntityManager
{
    private final GfxAsset[] blockTypes =
        {
            new GfxAsset
                (
                    GraphicID.G_SPIKE_BALL,
                    Settings._SPIKE_BALL,
                    GameAssets._SPIKE_BALL_1_ASSET,
                    GameAssets._SPIKE_BALL_FRAMES
                ),

            new GfxAsset
                (
                    GraphicID.G_SPIKE_BLOCK_HORIZONTAL,
                    Settings._SPIKE_BLOCK,
                    GameAssets._SPIKE_BLOCK_ASSET,
                    GameAssets._SPIKE_BLOCK_HORIZONTAL_FRAMES
                ),

            new GfxAsset
                (
                    GraphicID.G_SPIKE_BLOCK_VERTICAL,
                    Settings._SPIKE_BLOCK,
                    GameAssets._SPIKE_BLOCK_VERTICAL_ASSET,
                    GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES
                ),

            new GfxAsset
                (
                    GraphicID.G_LOOP_BLOCK_HORIZONTAL,
                    Settings._SPIKE_BLOCK,
                    GameAssets._LOOP_BLOCK_ASSET,
                    GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES
                ),

            new GfxAsset
                (
                    GraphicID.G_LOOP_BLOCK_VERTICAL,
                    Settings._SPIKE_BLOCK,
                    GameAssets._LOOP_BLOCK_ASSET,
                    GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES
                ),

            new GfxAsset
                (
                    GraphicID.G_FLOATING_PLATFORM,
                    Settings._DEFAULT_ON,
                    GameAssets._FLOATING_PLATFORM_ASSET,
                    GameAssets._FLOATING_PLATFORM_FRAMES
                ),
        };

    public BlocksManager(final App _app)
    {
        super(GraphicID._BLOCKS, "", _app);
    }

    @Override
    public void create()
    {
        Trace.__FILE_FUNC();

        for (GfxAsset gfxAsset : blockTypes)
        {
            graphicID = gfxAsset.graphicID;

            Array<MarkerTile> tiles = app.mapUtils.findMultiTiles(graphicID);

            if (app.entityUtils.canUpdate(graphicID)
                && Settings.isEnabled(gfxAsset.preference)
                && (tiles.size > 0))
            {
                for (MarkerTile tile : tiles)
                {
                    create
                        (
                            checkAssetName(gfxAsset).asset,
                            gfxAsset.frames,
                            Animation.PlayMode.LOOP,
                            tile._X,
                            tile._Y
                        );

                    entityDescriptor._SIZE  = GameAssets.getAssetSize(graphicID);
                    entityDescriptor._DIR.set(tile._DIR.getX(), tile._DIR.getY());
                    entityDescriptor._DIST  = tile._DIST;
                    entityDescriptor._SPEED = tile._SPEED;
                    entityDescriptor._LINK  = tile._LINK;

                    switch (gfxAsset.graphicID)
                    {
                        case G_SPIKE_BALL:
                        {
                            SpikeBall spikeBall = new SpikeBall(graphicID, app);
                            spikeBall.initialise(entityDescriptor);

                            app.entityData.addEntity(spikeBall);
                        }
                        break;

                        case G_SPIKE_BLOCK_HORIZONTAL:
                        case G_SPIKE_BLOCK_VERTICAL:
                        {
                            SpikeBlock spikeBlock = new SpikeBlock(graphicID, app);
                            spikeBlock.initialise(entityDescriptor);

                            app.entityData.addEntity(spikeBlock);
                        }
                        break;

                        case G_LOOP_BLOCK_HORIZONTAL:
                        case G_LOOP_BLOCK_VERTICAL:
                        {
                            LoopBlock loopBlock = new LoopBlock(graphicID, app);
                            loopBlock.initialise(entityDescriptor);

                            app.entityData.addEntity(loopBlock);
                        }
                        break;

                        case G_FLOATING_PLATFORM:
                        {
                            FloatingPlatform platform = new FloatingPlatform(graphicID, app);
                            platform.initialise(entityDescriptor);

                            app.entityData.addEntity(platform);
                        }
                        break;

                        default:
                            break;
                    }

                    EntityStats.log(graphicID);
                }
            }
        }
    }

    private static final String[] spikeBallAssets =
        {
            GameAssets._SPIKE_BALL_1_ASSET,
            GameAssets._SPIKE_BALL_2_ASSET,
            GameAssets._SPIKE_BALL_3_ASSET,
        };

    private GfxAsset checkAssetName(GfxAsset gfxAsset)
    {
        GfxAsset modifiedAsset = new GfxAsset(gfxAsset);

        if (gfxAsset.graphicID == GraphicID.G_SPIKE_BALL)
        {
            modifiedAsset.asset = spikeBallAssets[MathUtils.random(spikeBallAssets.length - 1)];
        }

        return modifiedAsset;
    }
}
