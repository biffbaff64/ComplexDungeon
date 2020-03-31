/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.red7projects.dungeon.map;

public class LayerImage
{
    public final String layerName;
    public final float  horizontalSpeed;
    public final float  verticalSpeed;

    public LayerImage(String _layerName, float _horizontalSpeed, float _verticalSpeed)
    {
        super();

        this.layerName          = _layerName;
        this.horizontalSpeed    = _horizontalSpeed;
        this.verticalSpeed      = _verticalSpeed;
    }
}
