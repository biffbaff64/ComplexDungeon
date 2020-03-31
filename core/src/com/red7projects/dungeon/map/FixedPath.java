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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.logging.Trace;

public class FixedPath
{
    public final int            pathNumber;
    public final Array<Vector2> data;

    public FixedPath(int _pathNum)
    {
        this.pathNumber = _pathNum;
        this.data = new Array<>();
    }

    public void debug()
    {
        Trace.__FILE_FUNC("Path Number: " + pathNumber);
        Trace.dbg("Number of entries: " + data.size);

        for (int i=0; i<data.size; i++)
        {
            Trace.dbg(data.get(i).toString());
        }
    }
}
