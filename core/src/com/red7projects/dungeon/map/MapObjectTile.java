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

package com.red7projects.dungeon.map;

import com.red7projects.dungeon.graphics.GraphicID;

public class MapObjectTile
{
    public final String    name;
    public final GraphicID gid;
    public final MarkerID  markerID;
    public final String    asset;

    public MapObjectTile(String _name, GraphicID _gid, MarkerID _markerID, String _asset)
    {
        this.name   = _name;
        this.gid    = _gid;
        this.markerID = _markerID;
        this.asset  = _asset;
    }
}
