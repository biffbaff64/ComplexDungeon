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

package com.red7projects.dungeon.entities.components;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class AbilityComponent
{
    public boolean running;
    public boolean fighting;
    public boolean jumping;
    public boolean flying;
    public boolean falling;
    public boolean pausing;
    public boolean followsPath;
    public boolean roaming;
    public boolean targetting;
    public boolean intelligence;
    public boolean hunter;

    public AbilityComponent()
    {
        super();

        this.running      = false;
        this.fighting     = false;
        this.jumping      = false;
        this.flying       = false;
        this.falling      = false;
        this.intelligence = false;
        this.pausing      = false;
        this.roaming      = false;
        this.targetting   = false;
        this.followsPath  = false;
        this.hunter       = false;
    }
}
