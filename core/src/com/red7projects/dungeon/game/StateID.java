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

package com.red7projects.dungeon.game;

@SuppressWarnings("unused")
public enum StateID
{
    _STATE_TITLE_SCREEN,
    _STATE_SETTINGS_SCREEN,
    _STATE_DEVELOPER_MENU_SCREEN,
    _STATE_CREDITS_SCREEN,
    _STATE_LOADING_SCREEN,
    _STATE_LEVEL_SELECT_SCREEN,
    _STATE_EXIT_SCREEN,
    _STATE_HISCORE_SCREEN,
    _STATE_ACHIEVEMENTS_SCREEN,
    _STATE_STATS_SCREEN,
    _STATE_PRIVACY_POLICY_SCREEN,
    _STATE_ENDGAME_SCREEN,

    _STATE_MENU_UPDATE,
    _STATE_SETUP,
    _STATE_GET_READY,
    _STATE_GAME,
    _STATE_PREPARE_GAME_END,
    _STATE_GAME_FINISHED,
    _STATE_GAME_OVER,
    _STATE_END_GAME,
    _STATE_PAUSED,
    _STATE_LEVEL_RETRY,
    _STATE_PREPARE_LEVEL_FINISHED,
    _STATE_LEVEL_FINISHED,
    _STATE_DEVELOPER_PANEL,
    _STATE_SETTINGS_PANEL,
    _STATE_ANNOUNCE_MISSILE,
    _STATE_UPDATE_MISSILE,
    _STATS_PLAY_ADVERT,
    _STATE_TELEPORTING,
    _STATE_NEW_HISCORE,

    _STATE_PANEL_START, _STATE_PANEL_INTRO, _STATE_PANEL_UPDATE, _STATE_PANEL_CLOSE,

    _INACTIVE, _LIMBO, _INIT, _UPDATE, _CLOSE,

    _STATE_OPEN, _STATE_OPENING, _STATE_CLOSING, _STATE_CLOSED,

    _STATE_TRIGGER_FADE_IN, _STATE_TRIGGER_FADE_OUT, _STATE_FADE_IN, _STATE_FADE_OUT,

    _STATE_ZOOM_IN, _STATE_ZOOM_OUT,

    _STATE_DEBUG_HANG,
}
