/*
 *  Copyright 02/06/2018 Red7Projects.
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

package com.red7projects.dungeon.game;

public class HighScore
{
    public int rank;
    public int level;
    public int score;
    public String name;

    public HighScore()
    {
        this.rank   = 0;
        this.level  = 0;
        this.score  = 0;
        this.name   = "";
    }

    public HighScore(int _rank, int _level, int _score, String _name)
    {
        this.rank   = _rank;
        this.level  = _level;
        this.score  = _score;
        this.name   = _name;
    }
}
