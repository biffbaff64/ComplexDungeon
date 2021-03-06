/*
 *  Copyright 07/05/2018 Red7Projects.
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

package com.red7projects.dungeon.physics.box2d;

import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.aabb.CollisionObject;

class BodyIdentity
{
    public final GraphicID gid;
    public final GraphicID type;
    public final GdxSprite entity;
    public final CollisionObject collisionObject;

    BodyIdentity(GdxSprite _entity, GraphicID _gid, GraphicID _type)
    {
        this.entity = _entity;
        this.gid = _gid;
        this.type = _type;
        this.collisionObject = _entity.collisionObject;
    }

    BodyIdentity(CollisionObject _collisionObject, GraphicID _gid, GraphicID _type)
    {
        this.entity = null;
        this.gid = _gid;
        this.type = _type;
        this.collisionObject = _collisionObject;
    }
}
