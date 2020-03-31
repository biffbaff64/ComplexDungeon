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

//@formatter:off
public class MapEntry
{
    public final String TMXFile;

    public final int maxLavaBalls;
    public final int maxBlueMines;
    public final int maxRedMines;
    public final int maxHunters;

    public MapEntry()
    {
        this.TMXFile        = "";
        this.maxLavaBalls   = 0;
        this.maxBlueMines   = 0;
        this.maxRedMines    = 0;
        this.maxHunters     = 0;
    }

    public MapEntry(final String _tmxFile,
                    final int _maxLavaBalls,
                    final int _maxBlueMines,
                    final int _maxRedMines,
                    final int _maxHunters
                    )
    {
        this.TMXFile        = _tmxFile;
        this.maxLavaBalls   = _maxLavaBalls;
        this.maxBlueMines   = _maxBlueMines;
        this.maxRedMines    = _maxRedMines;
        this.maxHunters     = _maxHunters;
    }
}
