/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.entities.components.EntityManagerComponent;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.ArrayList;

public class EntityData implements Disposable
{
    public Array<GdxSprite>                  entityMap;
    public ArrayList<EntityManagerComponent> managerList;

    public EntityData()
    {
    }

    public void createData()
    {
        Trace.__FILE_FUNC();

        entityMap   = new Array<>();
        managerList = new ArrayList<>();
    }

    public void update()
    {
        //
        // Scan through the entity map and update the
        // number of active entities for each type.
        int thisCount;

        for (final EntityManagerComponent managerComponent : managerList)
        {
            thisCount = 0;

            for (final GdxSprite gdxSprite : new Array.ArrayIterator<>(entityMap))
            {
                if (gdxSprite.gid == managerComponent.getGID())
                {
                    thisCount++;
                }
            }

            managerComponent.setActiveCount(thisCount);
        }
    }

    public int addManager(EntityManagerComponent manager)
    {
        managerList.add(manager);

        return managerList.size() - 1;
    }

    public void addEntity(GdxSprite spriteObject)
    {
        if (spriteObject != null)
        {
            entityMap.add(spriteObject);

            AppConfig.entitiesExist = true;
        }
        else
        {
            Trace.__FILE_FUNC("***** Attempt to add NULL Object, EntityMap current size: " + entityMap.size);
        }
    }

    public void removeEntity(int index)
    {
        entityMap.removeIndex(index);
    }

    @Override
    public void dispose()
    {
        for (int i = 0; i < entityMap.size; i++)
        {
            if (entityMap.get(i) != null)
            {
                entityMap.get(i).dispose();
            }
        }

        entityMap.clear();
        managerList.clear();

        entityMap   = null;
        managerList = null;
    }
}

