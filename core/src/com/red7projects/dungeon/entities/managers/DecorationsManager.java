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
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.Entities;
import com.red7projects.dungeon.entities.EntityStats;
import com.red7projects.dungeon.entities.characters.Decoration;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2;

//@formatter:off
public class DecorationsManager extends GenericEntityManager
{
    private static final String[] barrels =
        {
            GameAssets._BARREL_1_ASSET,
            GameAssets._BARREL_2_ASSET,
            GameAssets._BARREL_3_ASSET,
            GameAssets._BARREL_4_ASSET
        };

    private static final String[] pots =
        {
            GameAssets._POT_1_ASSET,
            GameAssets._POT_2_ASSET,
            GameAssets._POT_3_ASSET,
            GameAssets._POT_4_ASSET,
        };

    public DecorationsManager(final App _app)
    {
        super(GraphicID._DECORATION, "", _app);
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
            if (entityDef.type.equals(GraphicID._DECORATION))
            {
                graphicID = entityDef.graphicID;

                Array<SimpleVec2> coords = findMultiCoordinates(graphicID);

                for (SimpleVec2 coord : coords)
                {
                    String asset;

                    if (graphicID == GraphicID.G_BARREL)
                    {
                        asset = barrels[MathUtils.random(barrels.length-1)];
                    }
                    else if (graphicID == GraphicID.G_POT)
                    {
                        asset = pots[MathUtils.random(pots.length-1)];
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
                            coord.getX(),
                            coord.getY()
                        );

                    entityDescriptor._SIZE = GameAssets.getAssetSize(graphicID);

                    Decoration decoration = new Decoration(graphicID, app);
                    decoration.initialise(entityDescriptor);

                    app.entityData.addEntity(decoration);

                    EntityStats.log(graphicID);
                }
            }
        }
    }
}
