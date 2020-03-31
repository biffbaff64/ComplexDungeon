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

package com.red7projects.dungeon.screens;

/*
 * Screen identities
 */
public enum ScreenID
{
    _BASE_SCREEN(0),
    _RED7_SCREEN(1),
    _TITLE_SCREEN(2),
    _SETTINGS_SCREEN(3),
    _SETTINGS_MENU_SCREEN(4),
    _LEVEL_SELECT_SCREEN(5),
    _INSTRUCTIONS_SCREEN(6),
    _CREDITS_SCREEN(7),
    _LOADING_SCREEN(8),
    _GAME_SCREEN(9),
    _EXIT_SCREEN(10),
    _BONUS_SCREEN(11),
    _END_LEVEL_SCREEN(12),
    _HELP_SCREEN(13),
    _HISCORE_SCREEN(14),
    _ACHIEVEMENTS_SCREEN(15),
    _STATS_SCREEN(16),
    _KEYBOARD_OPTIONS_SCREEN(17),
    _PRIVACY_POLICY_SCREEN(18),
    _TEST_PANEL(19),
    _DEVELOPER_PANEL(20),
    _END_GAME_SCREEN(21),

    _BASE_RENDERER(100),
    _TITLE_RENDERER(101),
    _GAME_RENDERER(102),

    _UNKNOWN(255);

    private final int screenNumber;

    ScreenID(int value)
    {
        screenNumber = value;
    }

    public int getScreenNumber()
    {
        return screenNumber;
    }
}
