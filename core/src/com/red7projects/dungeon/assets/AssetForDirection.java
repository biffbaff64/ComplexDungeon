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

import com.red7projects.dungeon.physics.Movement;

/**
 * Matches a graphical asset with a Movement.Dir direction.
 * Useful for maps of assets which differ depending upon
 * direction of movement.
 */
public class AssetForDirection
{
    public final Movement.Dir direction;
    public final String   asset;

    public AssetForDirection(final Movement.Dir _direction, final String _asset)
    {
        this.direction = _direction;
        this.asset = _asset;
    }
}
