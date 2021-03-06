/*
 *  Copyright 06/05/2018 Red7Projects.
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

import com.badlogic.gdx.physics.box2d.World;

@SuppressWarnings("FieldCanBeLocal")
public class BodyFactory
{
    private final  World       world;
    private static BodyFactory thisInstance;

    private BodyFactory(World world)
    {
        this.world = world;
    }

    public static BodyFactory getInstance(World world)
    {
        if (thisInstance == null)
        {
            thisInstance = new BodyFactory(world);
        }

        return thisInstance;
    }
}
