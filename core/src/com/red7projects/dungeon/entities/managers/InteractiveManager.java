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
import com.red7projects.dungeon.entities.Entities;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.*;
import com.red7projects.dungeon.entities.characters.interactive.Door;
import com.red7projects.dungeon.entities.characters.interactive.Escalator;
import com.red7projects.dungeon.entities.characters.interactive.FloorSwitch;
import com.red7projects.dungeon.entities.characters.interactive.TreasureChest;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.map.TileID;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.utils.logging.Trace;

public class InteractiveManager extends GenericEntityManager
{
    public InteractiveManager(final App _app)
    {
        super(GraphicID._INTERACTIVE, "", _app);
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

        for (EntityDef entityDef : Entities.entityList)
        {
            if (entityDef.type.equals(GraphicID._INTERACTIVE))
            {
                graphicID = entityDef.graphicID;

                Array<MarkerTile> markers = app.mapUtils.findMultiTiles(graphicID);

                for (MarkerTile tile : markers)
                {
                    SimpleVec2 coord = new SimpleVec2(tile._X, tile._Y);

                    super.create
                        (
                            validateAsset(entityDef),
                            entityDef.frames,
                            (entityDef.frames > 1) ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL,
                            coord.getX(),
                            coord.getY()
                        );

                    entityDescriptor._LINK = tile._LINK;
                    entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                    if (graphicID == GraphicID.G_DOOR)
                    {
                        Door door = new Door(graphicID, app);

                        door.initialise(entityDescriptor);
                        app.entityData.addEntity(door);
                    }
                    else
                    {
                        GdxSprite interactive;

                        switch (graphicID)
                        {
                            case G_FLOOR_BUTTON:
                            case G_LEVER_SWITCH:
                            {
                                interactive = new FloorSwitch(graphicID, app);
                            }
                            break;

                            case G_TREASURE_CHEST:
                            case G_MYSTERY_CHEST:
                            {
                                interactive = new TreasureChest(graphicID, app);
                            }
                            break;

                            case G_ESCALATOR_LEFT:
                            case G_ESCALATOR_RIGHT:
                            case G_ESCALATOR_UP:
                            case G_ESCALATOR_DOWN:
                            case G_ESCALATOR:
                            {
                                interactive = new Escalator(graphicID, app);
                            }
                            break;

                            default:
                            {
                                interactive = new Decoration(graphicID, app);
                            }
                            break;
                        }

                        interactive.initialise(entityDescriptor);
                        app.entityData.addEntity(interactive);
                    }

                    EntityStats.log(graphicID);
                }
            }
        }
    }

    public String validateAsset(EntityDef _entityDef)
    {
        if (_entityDef.graphicID == GraphicID.G_ESCALATOR)
        {
            if (_entityDef.tileID == TileID._ESCALATOR_DOWN_TILE)
            {
                _entityDef.asset = GameAssets._ESCALATOR_DOWN_ASSET;
            }
            else if (_entityDef.tileID == TileID._ESCALATOR_UP_TILE)
            {
                _entityDef.asset = GameAssets._ESCALATOR_UP_ASSET;
            }
            else if (_entityDef.tileID == TileID._ESCALATOR_LEFT_TILE)
            {
                _entityDef.asset = GameAssets._ESCALATOR_LEFT_ASSET;
            }
            else
            {
                _entityDef.asset = GameAssets._ESCALATOR_RIGHT_ASSET;
            }
        }

        return  _entityDef.asset;
    }
}
