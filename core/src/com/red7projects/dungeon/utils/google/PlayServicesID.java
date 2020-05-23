/*
 *  Copyright 24/04/2018 Red7Projects.
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

package com.red7projects.dungeon.utils.google;

public enum PlayServicesID
{
    // achievements
    achievement_base_destroyed      ("CgkIvdearaEYEAIQBA"),
    achievement_millionaire         ("CgkIvdearaEYEAIQBQ"),
    achievement_score_500k          ("CgkIvdearaEYEAIQBg"),
    achievement_score_100k          ("CgkIvdearaEYEAIQBw"),
    achievement_shoot_a_missile     ("CgkIvdearaEYEAIQCA"),
    achievement_courier_services    ("CgkIvdearaEYEAIQCQ"),
    achievement_bridge_building     ("CgkIvdearaEYEAIQDQ"),
    achievement_beam_me_up          ("CgkIvdearaEYEAIQCg"),
    achievement_gunman_jetman       ("CgkIvdearaEYEAIQCw"),
    achievement_bomb_collector      ("CgkIvdearaEYEAIQDA"),
    achievement_moon_rider          ("CgkIvdearaEYEAIQDg"),

    // leaderboard High Scores
    leaderboard_leaderboard         ("CgkIvdearaEYEAIQAQ"),
    leaderboard_leaderboard_tester  ("CgkIvdearaEYEAIQAw");

    final String ID;

    PlayServicesID(String _ID)
    {
        this.ID = _ID;
    }

    public String getID()
    {
        return ID;
    }
}
