/*
 *  Copyright 01/05/2018 Red7Projects.
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

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class PolygonCollisionObject implements Disposable
{
    /*
     * Collision box statuc.
     *
     * _COLLIDABLE  -   Collidable but NOT in collision.
     * _INACTIVE    -   Permanently Invisible to any collidable objects.
     * _COLLIDING   -   In Collision.
     * _DEAD        -   To be removed from the list.
     */
    public Actions   action;
    public GraphicID gid;               // ID of THIS object
    public GraphicID type;              // _OBSTACLE or _ENTITY
    public Polygon   polygon;           // The actual collision polygon
    public int       index;             // This objects position in the collision object arraylist
    public short     bodyCategory;
    public short     collidesWith;

    public PolygonCollisionObject()
    {
        this.polygon = new Polygon();

        create();
    }

    private void create()
    {
        clearCollision();

        index        = AABBData.polygons().size;
        gid          = GraphicID.G_NO_ID;
        type         = GraphicID.G_NO_ID;
        action       = Actions._COLLIDABLE;
        bodyCategory = 0;
        collidesWith = 0;
    }

    public void addObjectToList()
    {
        AABBData.polygons().add(this);
    }

    public void removeObjectFromList()
    {
        AABBData.polygons().removeIndex(index);
        AABBData.rescanPolygons();
    }

    public void kill()
    {
        action = Actions._DEAD;
    }

    public void clearCollision()
    {
        action = Actions._COLLIDABLE;
    }

    @Override
    public void dispose()
    {
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("GID   : " + gid);
        Trace.dbg("ACTION: " + action);
    }
}
