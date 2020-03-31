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
import com.red7projects.dungeon.entities.Entities;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.*;
import com.red7projects.dungeon.entities.characters.interactive.Door;
import com.red7projects.dungeon.entities.characters.interactive.FloorSwitch;
import com.red7projects.dungeon.entities.characters.interactive.TreasureChest;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.maths.SimpleVec2;

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
                            entityDef.asset,
                            entityDef.frames,
                            (entityDef.frames > 1) ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL,
                            coord.x,
                            coord.y
                        );

                    entityDescriptor._LINK = tile._LINK;
                    entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                    if (graphicID == GraphicID.G_DOOR)
                    {
                        if (app.preferences.isEnabled(Preferences._DOORS))
                        {
                            Door door = new Door(graphicID, app);

                            door.initialise(entityDescriptor);
                            app.entityData.addEntity(door);
                        }
                    }
                    else
                    {
                        GdxSprite interactive;

                        if ((graphicID == GraphicID.G_FLOOR_BUTTON) || (graphicID == GraphicID.G_LEVER_SWITCH))
                        {
                            interactive = new FloorSwitch(graphicID, app);
                        }
                        else if ((graphicID == GraphicID.G_TREASURE_CHEST)
                            || (graphicID == GraphicID.G_MYSTERY_CHEST))
                        {
                            interactive = new TreasureChest(graphicID, app);
                        }
                        else
                        {
                            interactive = new Decoration(graphicID, app);
                        }

                        interactive.initialise(entityDescriptor);
                        app.entityData.addEntity(interactive);
                    }

                    EntityStats.log(graphicID);
                }
            }
        }
    }
}
