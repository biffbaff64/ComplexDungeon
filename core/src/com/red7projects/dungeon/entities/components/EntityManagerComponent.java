/*
 *  Copyright 01/12/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2;

public interface EntityManagerComponent
{
    void init();

    void update();

    void create();

    void create(String _asset, int _frames, Animation.PlayMode _mode, int x, int y);

    SimpleVec2 findCoordinates(final GraphicID targetGID);

    Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID);

    void free();

    void reset();

    int getActiveCount();

    void setActiveCount(int numActive);

    GraphicID getGID();

    boolean isPlaceable();

    void setPlaceable(boolean placeable);
}
