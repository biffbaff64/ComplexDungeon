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

package com.red7projects.dungeon.assets;

import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2;

/**
 * Matches graphics asset width & height
 * to a Graphic ID.
 */
public class AssetSize
{
    public final GraphicID  graphicID;
    public final SimpleVec2 size;

    public AssetSize(GraphicID _gid, int _width, int _height)
    {
        this.graphicID  = _gid;
        this.size       = new SimpleVec2(_width, _height);
    }
}
