/*
 *  Copyright 10/11/2018 Red7Projects.
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

package com.red7projects.dungeon.utils.logging.google;

public enum RCConstants
{
    RC_SIGN_IN          (9001),
    RC_UNALLOCATED_2    (9002),
    RC_ACHIEVEMENT_UI   (9003),
    RC_LEADERBOARD_UI   (9004),
    RC_UNALLOCATED_5    (9005),
    RC_SELECT_PLAYERS   (9006),
    RC_WAITING_ROOM     (9007),
    RC_INVITATION_INBOX (9008),
    RC_SAVED_GAMES_UI   (9009),
    RC_UNALLOCATED_10   (9010);

    public final int value;

    RCConstants(int _value)
    {
        value = _value;
    }
}
