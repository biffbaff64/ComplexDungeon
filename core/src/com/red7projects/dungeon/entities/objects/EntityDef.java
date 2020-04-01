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

package com.red7projects.dungeon.entities.objects;

import com.red7projects.dungeon.map.TileID;
import com.red7projects.dungeon.graphics.GraphicID;

public class EntityDef
{
    public String    objectName;
    public GraphicID graphicID;
    public TileID    tileID;
    public String    asset;
    public int       frames;
    public GraphicID type;

    public EntityDef()
    {
        this.objectName = "";
        this.graphicID = GraphicID.G_NO_ID;
        this.tileID    = TileID._DEFAULT_TILE;
        this.asset     = "";
        this.frames     = 0;
        this.type       = GraphicID.G_NO_ID;
    }

    public EntityDef(final String _objectName,
                     final GraphicID _graphicID,
                     final TileID _tileID,
                     final String _asset,
                     final int _frames,
                     final GraphicID _type)
    {
        this.objectName = _objectName;
        this.graphicID = _graphicID;
        this.tileID    = _tileID;
        this.asset     = _asset;
        this.frames     = _frames;
        this.type       = _type;
    }
}
