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

package com.red7projects.dungeon.assets;

import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SimpleVec2;

// @formatter:off
public class GameAssets
{
    //
    // MainPlayer assets
    public static final String _RUN_UP_ASSET         = "walk_up";
    public static final String _RUN_DOWN_ASSET       = "walk_down";
    public static final String _RUN_LEFT_ASSET       = "walk_left";
    public static final String _RUN_RIGHT_ASSET      = "walk_right";
    public static final String _RUN_UP_LEFT_ASSET    = "walk_up_left";
    public static final String _RUN_DOWN_LEFT_ASSET  = "walk_down_left";
    public static final String _RUN_UP_RIGHT_ASSET   = "walk_up_right";
    public static final String _RUN_DOWN_RIGHT_ASSET = "walk_down_right";

    public static final String _IDLE_UP_ASSET         = "stand_up";
    public static final String _IDLE_DOWN_ASSET       = "stand_down";
    public static final String _IDLE_LEFT_ASSET       = "stand_left";
    public static final String _IDLE_RIGHT_ASSET      = "stand_right";
    public static final String _IDLE_UP_LEFT_ASSET    = "stand_up_left";
    public static final String _IDLE_DOWN_LEFT_ASSET  = "stand_down_left";
    public static final String _IDLE_UP_RIGHT_ASSET   = "stand_up_right";
    public static final String _IDLE_DOWN_RIGHT_ASSET = "stand_down_right";

    public static final String _FIGHT_UP_ASSET         = "fight_up";
    public static final String _FIGHT_DOWN_ASSET       = "fight_down";
    public static final String _FIGHT_LEFT_ASSET       = "fight_left";
    public static final String _FIGHT_RIGHT_ASSET      = "fight_right";
    public static final String _FIGHT_UP_LEFT_ASSET    = "fight_up_left";
    public static final String _FIGHT_DOWN_LEFT_ASSET  = "fight_down_left";
    public static final String _FIGHT_UP_RIGHT_ASSET   = "fight_up_right";
    public static final String _FIGHT_DOWN_RIGHT_ASSET = "fight_down_right";

    public static final String _CAST_UP_ASSET         = "cast_up";
    public static final String _CAST_DOWN_ASSET       = "cast_down";
    public static final String _CAST_LEFT_ASSET       = "cast_left";
    public static final String _CAST_RIGHT_ASSET      = "cast_right";
    public static final String _CAST_UP_LEFT_ASSET    = "cast_up_left";
    public static final String _CAST_DOWN_LEFT_ASSET  = "cast_down_left";
    public static final String _CAST_UP_RIGHT_ASSET   = "cast_up_right";
    public static final String _CAST_DOWN_RIGHT_ASSET = "cast_down_right";

    public static final String _DYING_ASSET        = "ljm_death";
    public static final String _PLAYER_SPAWN_ASSET = "player_appear";

    public static final int _PLAYER_STAND_FRAMES = 7;
    public static final int _PLAYER_RUN_FRAMES   = 10;
    public static final int _PLAYER_FIGHT_FRAMES = 7;
    public static final int _PLAYER_CAST_FRAMES  = 6;
    public static final int _PLAYER_DYING_FRAMES = 1;
    public static final int _PLAYER_SPAWN_FRAMES = 20;
    public static final int _PLAYER_APPEAR_FRAME = 13;

    //
    // Prisoner assets
    public static final String _PRISONER_IDLE_UP_ASSET         = "prisoner_stand_up";
    public static final String _PRISONER_IDLE_DOWN_ASSET       = "prisoner_stand_down";
    public static final String _PRISONER_IDLE_LEFT_ASSET       = "prisoner_stand_left";
    public static final String _PRISONER_IDLE_RIGHT_ASSET      = "prisoner_stand_right";
    public static final String _PRISONER_IDLE_UP_LEFT_ASSET    = "prisoner_stand_up_left";
    public static final String _PRISONER_IDLE_UP_RIGHT_ASSET   = "prisoner_stand_up_right";
    public static final String _PRISONER_IDLE_DOWN_LEFT_ASSET  = "prisoner_stand_down_left";
    public static final String _PRISONER_IDLE_DOWN_RIGHT_ASSET = "prisoner_stand_down_right";

    public static final int _PRISONER_IDLE_FRAMES = 7;

    //
    // Villager assets
    public static final String _VILLAGER_IDLE_UP_ASSET         = "villager_stand_up";
    public static final String _VILLAGER_IDLE_DOWN_ASSET       = "villager_stand_down";
    public static final String _VILLAGER_IDLE_LEFT_ASSET       = "villager_stand_left";
    public static final String _VILLAGER_IDLE_RIGHT_ASSET      = "villager_stand_right";
    public static final String _VILLAGER_IDLE_UP_LEFT_ASSET    = "villager_stand_left";
    public static final String _VILLAGER_IDLE_UP_RIGHT_ASSET   = "villager_stand_right";
    public static final String _VILLAGER_IDLE_DOWN_LEFT_ASSET  = "villager_stand_left";
    public static final String _VILLAGER_IDLE_DOWN_RIGHT_ASSET = "villager_stand_right";

    public static final int _VILLAGER_IDLE_FRAMES = 8;

    //
    // Soldier assets
    public static final String _SOLDIER_RUN_UP_ASSET         = "soldier_walk_up";
    public static final String _SOLDIER_RUN_DOWN_ASSET       = "soldier_walk_down";
    public static final String _SOLDIER_RUN_LEFT_ASSET       = "soldier_walk_left";
    public static final String _SOLDIER_RUN_RIGHT_ASSET      = "soldier_walk_right";
    public static final String _SOLDIER_RUN_UP_LEFT_ASSET    = "soldier_walk_left";
    public static final String _SOLDIER_RUN_DOWN_LEFT_ASSET  = "soldier_walk_left";
    public static final String _SOLDIER_RUN_UP_RIGHT_ASSET   = "soldier_walk_right";
    public static final String _SOLDIER_RUN_DOWN_RIGHT_ASSET = "soldier_walk_right";

    public static final String _SOLDIER_FIGHT_UP_ASSET         = "soldier_fight_up";
    public static final String _SOLDIER_FIGHT_DOWN_ASSET       = "soldier_fight_down";
    public static final String _SOLDIER_FIGHT_LEFT_ASSET       = "soldier_fight_left";
    public static final String _SOLDIER_FIGHT_RIGHT_ASSET      = "soldier_fight_right";
    public static final String _SOLDIER_FIGHT_UP_LEFT_ASSET    = "soldier_fight_left";
    public static final String _SOLDIER_FIGHT_DOWN_LEFT_ASSET  = "soldier_fight_left";
    public static final String _SOLDIER_FIGHT_UP_RIGHT_ASSET   = "soldier_fight_right";
    public static final String _SOLDIER_FIGHT_DOWN_RIGHT_ASSET = "soldier_fight_right";

    public static final String _SOLDIER_IDLE_UP_ASSET         = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_DOWN_ASSET       = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_LEFT_ASSET       = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_RIGHT_ASSET      = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_UP_LEFT_ASSET    = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_UP_RIGHT_ASSET   = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_DOWN_LEFT_ASSET  = "soldier_stand_down";
    public static final String _SOLDIER_IDLE_DOWN_RIGHT_ASSET = "soldier_stand_down";

    public static final int _SOLDIER_IDLE_FRAMES  = 7;
    public static final int _SOLDIER_RUN_FRAMES   = 10;
    public static final int _SOLDIER_FIGHT_FRAMES = 6;

    //
    // Pickups
    public static final String _BLUE_GEMS_ASSET     = "gems_blue";
    public static final String _GREEN_GEMS_ASSET    = "gems";
    public static final String _GOLD_GEMS_ASSET     = "gems_gold";
    public static final String _RED_GEMS_ASSET      = "gems_red";
    public static final String _COIN_ASSET          = "coin";
    public static final String _KEY_ASSET           = "keys";
    public static final String _SHIELD_ASSET        = "shield";
    public static final String _RUNES_ASSET         = "runes";
    public static final String _GREY_RUNES_ASSET    = "grey_runes";
    public static final String _APPLE_ASSET         = "apple";
    public static final String _BOOK_ASSET          = "book";
    public static final String _CAKE_ASSET          = "cake";
    public static final String _CHERRIES_ASSET      = "cherries";
    public static final String _GRAPES_ASSET        = "grapes";
    public static final String _SILVER_ARMOUR_ASSET = "silver_armour";
    public static final String _GOLD_ARMOUR_ASSET   = "gold_armour";
    public static final String _GOLD_KEY_ASSET      = "key_held_panel";
    public static final String _DOCUMENT_ASSET      = "paper";
    public static final String _LITTER_ASSET        = "litter";
    public static final String _BOOKS_ASSET         = "books";


    //
    // Interactive Items
    public static final String _FLOATING_PLATFORM_ASSET     = "floating_plank";
    public static final String _FLOOR_BUTTON_ASSET          = "floor_button";
    public static final String _LEVER_SWITCH_ASSET          = "lever";
    public static final String _WOOD_DOOR_ASSET             = "wood_door";
    public static final String _LOCKED_DOOR_ASSET           = "locked_door";
    public static final String _TREASURE_CHEST_ASSET        = "treasure_chest";
    public static final String _MYSTERY_CHEST_ASSET         = "chest1";
    public static final String _TELEPORTER_ASSET            = "teleporter";
    public static final String _LASER_BEAM_VERTICAL_ASSET   = "laservert9";
    public static final String _LASER_BEAM_HORIZONTAL_ASSET = "laserhoriz9";
    public static final String _CROSSHAIRS_ASSET            = "crosshairs";
    public static final String _ESCALATOR_LEFT_ASSET        = "escalator_left";
    public static final String _ESCALATOR_RIGHT_ASSET       = "escalator_right";
    public static final String _ESCALATOR_UP_ASSET          = "escalator_up";
    public static final String _ESCALATOR_DOWN_ASSET        = "escalator_down";

    //
    // Decorations
    public static final String _ALCOVE_TORCH_ASSET = "alcove_torch";
    public static final String _POT_1_ASSET        = "pot1";
    public static final String _POT_2_ASSET        = "pot2";
    public static final String _POT_3_ASSET        = "pot3";
    public static final String _POT_4_ASSET        = "pot4";
    public static final String _CRATE_ASSET        = "crate";
    public static final String _BARREL_1_ASSET     = "barrel1";
    public static final String _BARREL_2_ASSET     = "barrel2";
    public static final String _BARREL_3_ASSET     = "barrel3";
    public static final String _BARREL_4_ASSET     = "barrel4";
    public static final String _SACKS_ASSET        = "sacks";

    //
    // Static Enemies
    public static final String _TURRET_ASSET               = "turret";
    public static final String _FLAME_THROW_ASSET          = "flame_thrower";
    public static final String _FLAME_THROW_VERTICAL_ASSET = "flame_thrower_vertical";

    //
    // Mobile Enemies
    public static final String _STORM_DEMON_ASSET          = "storm_demon";
    public static final String _BOUNCER_ASSET              = "bouncer";
    public static final String _FIRE_BALL_ASSET            = "fireball";
    public static final String _MINI_FIRE_BALL_ASSET       = "mini_fireball";
    public static final String _SCORPION_ASSET             = "scorpìon_walk";
    public static final String _SPIKE_BALL_1_ASSET         = "round2";
    public static final String _SPIKE_BALL_2_ASSET         = "round2";
    public static final String _SPIKE_BALL_3_ASSET         = "round2";
    public static final String _SPIKE_BLOCK_ASSET          = "spinning_spikes";
    public static final String _SPIKE_BLOCK_VERTICAL_ASSET = "spinning_spikes_vertical";
    public static final String _LOOP_BLOCK_ASSET           = "spinning_spikes_vertical";

    //
    // In-Game Messaging
    public static final String _HELP_ME_ASSET             = "help_bubble";
    public static final String _ABXY_ASSET                = "abxy";
    public static final String _WELCOME_MSG_ASSET         = "welcome_message";
    public static final String _KEY_COLLECTED_MSG_ASSET   = "key_collected";
    public static final String _GAMEOVER_MSG_ASSET        = "gameover";
    public static final String _KEY_NEEDED_MSG_ASSET      = "key_needed";
    public static final String _KEY_HINT_MSG_ASSET        = "key_hint";
    public static final String _GETREADY_MSG_ASSET        = "getready";
    public static final String _PRESS_FOR_TREASURE_ASSET  = "press_for_treasure";
    public static final String _PRESS_FOR_PRISONER_ASSET  = "press_for_prisoner";
    public static final String _STORM_DEMON_WARNING_ASSET = "storm_demon_warning";
    public static final String _VILLAGER_FACES_ASSET      = "villager_faces";

    //
    // Weaponry and Explosions
    public static final String _EXPLOSION64_ASSET     = "explosion64";
    public static final String _LASER_ASSET           = "player_laser";
    public static final String _ARROW_ASSET           = "arrows";
    public static final String _PHASER_BULLET_ASSET   = "bullet1";
    public static final String _FIREBALL_BULLET_ASSET = "bullet2";

    //
    // Frame counts for animations
    public static final int _LASER_BEAM_FRAMES             = 1;
    public static final int _EXPLOSION64_FRAMES            = 12;
    public static final int _LASER_FRAMES                  = 1;
    public static final int _ARROW_FRAMES                  = 1;
    public static final int _COIN_FRAMES                   = 4;
    public static final int _GEMS_FRAMES                   = 6;
    public static final int _KEY_FRAMES                    = 5;
    public static final int _SHIELD_FRAMES                 = 1;
    public static final int _APPLE_FRAMES                  = 1;
    public static final int _BOOK_FRAMES                   = 1;
    public static final int _CAKE_FRAMES                   = 1;
    public static final int _CHERRIES_FRAMES               = 1;
    public static final int _GRAPES_FRAMES                 = 1;
    public static final int _ARMOUR_FRAMES                 = 1;
    public static final int _ALCOVE_TORCH_FRAMES           = 4;
    public static final int _POT_FRAMES                    = 1;
    public static final int _CRATE_FRAMES                  = 1;
    public static final int _FLOOR_BUTTON_FRAMES           = 2;
    public static final int _LEVER_SWITCH_FRAMES           = 4;
    public static final int _BARREL_FRAMES                 = 1;
    public static final int _DOOR_FRAMES                   = 3;
    public static final int _LOCKED_DOOR_FRAMES            = 1;
    public static final int _TREASURE_CHEST_FRAMES         = 4;
    public static final int _MYSTERY_CHEST_FRAMES          = 2;
    public static final int _STORM_DEMON_FRAMES            = 7;
    public static final int _BOUNCER_FRAMES                = 1;
    public static final int _JELLY_MONSTER_FRAMES          = 8;
    public static final int _MINI_FIRE_BALL_FRAMES         = 6;
    public static final int _FLAME_THROW_FRAMES            = 4;
    public static final int _SPIKE_BALL_FRAMES             = 1;
    public static final int _SPIKE_BLOCK_HORIZONTAL_FRAMES = 10;
    public static final int _SPIKE_BLOCK_VERTICAL_FRAMES   = 10;
    public static final int _TELEPORTER_FRAMES             = 8;
    public static final int _HELP_ME_FRAMES                = 1;
    public static final int _PHASER_BULLET_FRAMES          = 1;
    public static final int _FIREBALL_BULLET_FRAMES        = 1;
    public static final int _DOCUMENT_FRAMES               = 6;
    public static final int _SCORPION_FRAMES               = 15;
    public static final int _GREEN_BEETLE_FRAMES           = 5;
    public static final int _QUESTION_MARK_FRAMES          = 1;
    public static final int _FLOATING_PLATFORM_FRAMES      = 1;
    public static final int _TURRET_FRAMES                 = 1;
    public static final int _PRESS_BUTTON_MESSAGES_FRAMES  = 3;
    public static final int _SACKS_FRAMES                  = 1;
    public static final int _CROSSHAIRS_FRAMES             = 1;
    public static final int _ESCALATOR_FRAMES              = 16;
    public static final int _VILLAGER_FACES_FRAMES         = 3;
    public static final int _LITTER_FRAMES                 = 10;
    public static final int _BOOKS_FRAMES                  = 5;
    public static final int _RUNES_FRAMES                  = 8;

    //
    // Fonts and HUD assets
    public static final String _GAME_BACKGROUND     = "data/water_background.png";
    public static final String _CENTURY_FONT        = "data/fonts/CENSCBK.ttf";
    public static final String _ACME_FONT           = "data/fonts/Acme-Regular.ttf";
    public static final String _PRO_WINDOWS_FONT    = "data/fonts/ProFontWindows.ttf";
    public static final String _HUD_PANEL_FONT      = "data/fonts/Acme-Regular.ttf";
    public static final String _HUD_PANEL_ASSET     = "data/hud_panel_rework.png";
    public static final String _MESSAGE_PANEL_ASSET = "message_panel";

    public static int hudPanelWidth;      // Set when object is loaded
    public static int hudPanelHeight;     //

    private static final AssetSize[] assetSizes =
        {
            new AssetSize(GraphicID.G_PLAYER, 82, 120),
            new AssetSize(GraphicID.G_PLAYER_FIGHT, 82, 120),
            new AssetSize(GraphicID.G_PLAYER_CAST, 82, 120),
            new AssetSize(GraphicID.G_ARROW, 82, 82),
            new AssetSize(GraphicID.G_PRISONER, 128, 128),
            new AssetSize(GraphicID.G_VILLAGER, 128, 128),
            new AssetSize(GraphicID.G_SOLDIER, 148, 148),
            new AssetSize(GraphicID.G_SOLDIER_FIGHT, 228, 228),

            new AssetSize(GraphicID.G_EXPLOSION12, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION32, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION64, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION128, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION256, 64, 64),

            // Pickup Items
            new AssetSize(GraphicID.G_ARROW, 64, 64),
            new AssetSize(GraphicID.G_GEM, 76, 64),
            new AssetSize(GraphicID.G_COIN, 64, 64),
            new AssetSize(GraphicID.G_SHIELD, 64, 78),
            new AssetSize(GraphicID.G_KEY, 64, 64),
            new AssetSize(GraphicID.G_HUD_KEY, 96, 96),
            new AssetSize(GraphicID.G_DOCUMENT, 122, 128),
            new AssetSize(GraphicID.G_APPLE, 64, 64),
            new AssetSize(GraphicID.G_BOOK, 64, 64),
            new AssetSize(GraphicID.G_LITTER, 64, 64),
            new AssetSize(GraphicID.G_RUNE, 64, 64),
            new AssetSize(GraphicID.G_CAKE, 64, 64),
            new AssetSize(GraphicID.G_CHERRIES, 64, 64),
            new AssetSize(GraphicID.G_GRAPES, 64, 64),
            new AssetSize(GraphicID.G_SILVER_ARMOUR, 64, 64),
            new AssetSize(GraphicID.G_GOLD_ARMOUR, 64, 64),

            // Decorations
            new AssetSize(GraphicID.G_POT, 93, 128),
            new AssetSize(GraphicID.G_CRATE, 98, 120),
            new AssetSize(GraphicID.G_BARREL, 90, 152),
            new AssetSize(GraphicID.G_GLOW_EYES, 71, 64),
            new AssetSize(GraphicID.G_ALCOVE_TORCH, 64, 192),
            new AssetSize(GraphicID.G_SACKS, 124, 170),

            // Interactive items
            new AssetSize(GraphicID.G_FLOOR_BUTTON, 64, 64),
            new AssetSize(GraphicID.G_LEVER_SWITCH, 82, 79),
            new AssetSize(GraphicID.G_TREASURE_CHEST, 142, 142),
            new AssetSize(GraphicID.G_MYSTERY_CHEST, 128, 142),
            new AssetSize(GraphicID.G_DOOR, 512, 384),
            new AssetSize(GraphicID.G_LOCKED_DOOR, 512, 384),
            new AssetSize(GraphicID.G_TELEPORTER, 64, 64),
            new AssetSize(GraphicID.G_HELP_BUBBLE, 192, 96),
            new AssetSize(GraphicID.G_QUESTION_MARK, 128, 80),
            new AssetSize(GraphicID.G_EXCLAMATION_MARK, 30, 48),
            new AssetSize(GraphicID.G_FLOATING_PLATFORM, 128, 128),
            new AssetSize(GraphicID.G_CROSSHAIRS, 64, 64),
            new AssetSize(GraphicID.G_ESCALATOR, 64, 64),
            new AssetSize(GraphicID.G_ESCALATOR_LEFT, 64, 64),
            new AssetSize(GraphicID.G_ESCALATOR_RIGHT, 64, 64),
            new AssetSize(GraphicID.G_ESCALATOR_UP, 64, 64),
            new AssetSize(GraphicID.G_ESCALATOR_DOWN, 64, 64),

            // Messages
            new AssetSize(GraphicID._PRESS_FOR_TREASURE, 1024, 114),
            new AssetSize(GraphicID._PRESS_FOR_PRISONER, 1024, 114),
            new AssetSize(GraphicID._PRESS_FOR_GUIDE, 1024, 59),
            new AssetSize(GraphicID._STORM_DEMON_WARNING, 1660, 50),
            new AssetSize(GraphicID._KEY_NEEDED, 1024, 114),

            // Stationary enemies
            new AssetSize(GraphicID.G_LASER_BEAM_VERTICAL, 64, 64),
            new AssetSize(GraphicID.G_LASER_BEAM_HORIZONTAL, 64, 64),
            new AssetSize(GraphicID.G_FLAME_THROWER, 636, 334),
            new AssetSize(GraphicID.G_FLAME_THROWER_VERTICAL, 334, 636),
            new AssetSize(GraphicID.G_TURRET, 128, 128),

            // Mobile enemies
            new AssetSize(GraphicID.G_STORM_DEMON, 192, 192),
            new AssetSize(GraphicID.G_BOUNCER, 96, 96),
            new AssetSize(GraphicID.G_SPIKE_BALL, 128, 128),
            new AssetSize(GraphicID.G_SPIKE_BLOCK_HORIZONTAL, 147, 256),
            new AssetSize(GraphicID.G_SPIKE_BLOCK_VERTICAL, 256, 147),
            new AssetSize(GraphicID.G_LOOP_BLOCK_HORIZONTAL, 147, 256),
            new AssetSize(GraphicID.G_LOOP_BLOCK_VERTICAL, 256, 147),
            new AssetSize(GraphicID.G_ENEMY_BULLET, 32, 18),
            new AssetSize(GraphicID.G_ENEMY_FIREBALL, 64, 64),
            new AssetSize(GraphicID.G_MINI_FIRE_BALL, 64, 64),
            new AssetSize(GraphicID.G_SCORPION, 136, 123),
        };

    private GameAssets() {}

    public static SimpleVec2 getAssetSize(GraphicID _gid)
    {
        SimpleVec2 size = new SimpleVec2();

        for (final AssetSize assetSize : assetSizes)
        {
            if (assetSize.graphicID == _gid)
            {
                size = assetSize.size;
            }
        }

        return size;
    }
}
