/*
 *  Copyright 16/09/2018 Red7Projects.
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

package com.red7projects.dungeon.physics.aabb;

import com.badlogic.gdx.utils.Array;

@SuppressWarnings("WeakerAccess")
public class AABBData
{
    public static final short _TOP       = 0x01;
    public static final short _BOTTOM    = 0x02;
    public static final short _LEFT      = 0x04;
    public static final short _RIGHT     = 0x08;

    private static Array<CollisionObject> collisionBoxData;
    private static Array<PolygonCollisionObject> collisionPolygonData;

    public static void createData()
    {
        collisionBoxData = new Array<>();
        collisionPolygonData = new Array<>();
    }

    public static Array<CollisionObject> boxes()
    {
        return collisionBoxData;
    }

    public static Array<PolygonCollisionObject> polygons()
    {
        return collisionPolygonData;
    }

    /**
     * Rescan the collision object list, re-ordering indexes
     * after an object has been removed.
     */
    public static void rescan()
    {
        for (int i = 0; i < collisionBoxData.size; i++)
        {
            collisionBoxData.get(i).index = i;
        }
    }

    public static void rescanPolygons()
    {
        for (int i = 0; i < collisionPolygonData.size; i++)
        {
            collisionPolygonData.get(i).index = i;
        }
    }
}
