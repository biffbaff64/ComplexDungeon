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

package com.red7projects.dungeon.entities;

import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.MarkerID;

@SuppressWarnings("WeakerAccess")
//formatter:off
public abstract class Entities
{
    public static final EntityDef[] entityList =
        {
            // Main Characters
            new EntityDef("", GraphicID.G_PLAYER, MarkerID._PLAYER_TILE, GameAssets._IDLE_DOWN_ASSET, GameAssets._PLAYER_STAND_FRAMES, GraphicID._MAIN),
            new EntityDef("Prisoner", GraphicID.G_PRISONER, MarkerID._PRISONER_TILE, GameAssets._PRISONER_IDLE_DOWN_ASSET, GameAssets._PRISONER_IDLE_FRAMES, GraphicID._MAIN),
            new EntityDef("Villager", GraphicID.G_VILLAGER, MarkerID._VILLAGER_TILE, GameAssets._VILLAGER_IDLE_DOWN_ASSET, GameAssets._VILLAGER_IDLE_FRAMES, GraphicID._MAIN),

            // Pickups
            new EntityDef("", GraphicID.G_ARROW, MarkerID._ARROW_TILE, GameAssets._ARROW_ASSET, GameAssets._ARROW_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_GEM, MarkerID._GEM_TILE, GameAssets._BLUE_GEMS_ASSET, GameAssets._GEMS_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_COIN, MarkerID._COIN_TILE, GameAssets._COIN_ASSET, GameAssets._COIN_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_SPECIAL_COIN, MarkerID._SPECIAL_COIN_TILE, GameAssets._COIN_ASSET, GameAssets._COIN_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_HIDDEN_COIN, MarkerID._HIDDEN_COIN_TILE, GameAssets._COIN_ASSET, GameAssets._COIN_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_SHIELD, MarkerID._SHIELD_TILE, GameAssets._SHIELD_ASSET, GameAssets._SHIELD_FRAMES, GraphicID._PICKUP),
            new EntityDef("", GraphicID.G_KEY, MarkerID._KEY_TILE, GameAssets._KEY_ASSET, GameAssets._KEY_FRAMES, GraphicID._PICKUP),
//            G_HUD_KEY,
//            G_APPLE,
//            G_BOOK,
//            G_CAKE,
//            G_CHERRIES,
//            G_GRAPES,
//            G_SILVER_ARMOUR,
//            G_GOLD_ARMOUR,

            // Decorations
            new EntityDef("", GraphicID.G_ALCOVE_TORCH, MarkerID._TORCH2_TILE, GameAssets._ALCOVE_TORCH_ASSET, GameAssets._ALCOVE_TORCH_FRAMES, GraphicID._DECORATION),
            new EntityDef("", GraphicID.G_POT, MarkerID._POT_TILE, GameAssets._POT_1_ASSET, GameAssets._POT_FRAMES, GraphicID._DECORATION),
            new EntityDef("", GraphicID.G_CRATE, MarkerID._CRATE_TILE, GameAssets._CRATE_ASSET, GameAssets._CRATE_FRAMES, GraphicID._DECORATION),
            new EntityDef("", GraphicID.G_BARREL, MarkerID._BARREL_TILE, GameAssets._BARREL_1_ASSET, GameAssets._BARREL_FRAMES, GraphicID._DECORATION),

            // Interactive, But NOT Pickups
            new EntityDef("", GraphicID.G_TREASURE_CHEST, MarkerID._CHEST_TILE, GameAssets._TREASURE_CHEST_ASSET, GameAssets._TREASURE_CHEST_FRAMES, GraphicID._INTERACTIVE),
            new EntityDef("Mystery Box", GraphicID.G_MYSTERY_CHEST, MarkerID._MYSTERY_CHEST_TILE, GameAssets._MYSTERY_CHEST_ASSET, GameAssets._MYSTERY_CHEST_FRAMES, GraphicID._INTERACTIVE),
            new EntityDef("Door", GraphicID.G_DOOR, MarkerID._DOOR_TILE, GameAssets._WOOD_DOOR_ASSET, GameAssets._DOOR_FRAMES, GraphicID._INTERACTIVE),
            new EntityDef("Floor Button", GraphicID.G_FLOOR_BUTTON, MarkerID._FLOOR_BUTTON_TILE, GameAssets._FLOOR_BUTTON_ASSET, GameAssets._FLOOR_BUTTON_FRAMES, GraphicID._INTERACTIVE),
            new EntityDef("Lever Switch", GraphicID.G_LEVER_SWITCH, MarkerID._LEVER_TILE, GameAssets._LEVER_SWITCH_ASSET, GameAssets._LEVER_SWITCH_FRAMES, GraphicID._INTERACTIVE),
            new EntityDef("", GraphicID.G_TELEPORTER, MarkerID._UNKNOWN, GameAssets._TELEPORTER_ASSET, GameAssets._TELEPORTER_FRAMES, GraphicID._INTERACTIVE),

            // Interactive, but not handled by InteractiveManager
            new EntityDef("Floating Platform", GraphicID.G_FLOATING_PLATFORM, MarkerID._FLOATING_PLATFORM_TILE, GameAssets._FLOATING_PLATFORM_ASSET, GameAssets._FLOATING_PLATFORM_FRAMES, GraphicID._PLATFORM),
//            G_TALK_BOX,

            // Stationary Enemies
            new EntityDef("Laser", GraphicID.G_LASER_BEAM, MarkerID._LASER_TILE, GameAssets._LASER_BEAM_HORIZONTAL_ASSET, GameAssets._LASER_BEAM_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_LASER_BEAM_HORIZONTAL, MarkerID._LASER_HORIZONTAL_TILE, GameAssets._LASER_BEAM_HORIZONTAL_ASSET, GameAssets._LASER_BEAM_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_LASER_BEAM_VERTICAL, MarkerID._LASER_VERTICAL_TILE, GameAssets._LASER_BEAM_VERTICAL_ASSET, GameAssets._LASER_BEAM_FRAMES, GraphicID._ENEMY),
            new EntityDef("Flame Thrower", GraphicID.G_FLAME_THROWER, MarkerID._FLAME_THROWER_TILE, GameAssets._FLAME_THROW_ASSET, GameAssets._FLAME_THROW_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_FLAME_THROWER_VERTICAL, MarkerID._FLAME_THROWER_TILE, GameAssets._FLAME_THROW_VERTICAL_ASSET, GameAssets._FLAME_THROW_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_TURRET, MarkerID._TURRET_TILE, GameAssets._TURRET_ASSET, GameAssets._TURRET_FRAMES, GraphicID._ENEMY),

            // Mobile Enemies
            new EntityDef("", GraphicID.G_STORM_DEMON, MarkerID._STORM_DEMON_TILE, GameAssets._STORM_DEMON_ASSET, GameAssets._STORM_DEMON_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_BOUNCER, MarkerID._BOUNCER_TILE, GameAssets._BOUNCER_ASSET, GameAssets._BOUNCER_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_SCORPION, MarkerID._SCORPION_TILE, GameAssets._SCORPION_ASSET, GameAssets._SCORPION_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_BEETLE, MarkerID._BEETLE_TILE, GameAssets._GREEN_BEETLE_ASSET, GameAssets._GREEN_BEETLE_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_JELLY_MONSTER, MarkerID._JELLY_MONSTER_TILE, GameAssets._JELLY_MONSTER_ASSET, GameAssets._JELLY_MONSTER_FRAMES, GraphicID._ENEMY),
            new EntityDef("", GraphicID.G_SOLDIER, MarkerID._SOLDIER_TILE, GameAssets._SOLDIER_IDLE_DOWN_ASSET, GameAssets._SOLDIER_IDLE_FRAMES, GraphicID._ENEMY),
            new EntityDef("Spike Ball", GraphicID.G_SPIKE_BALL, MarkerID._SPIKE_BALL_TILE, GameAssets._SPIKE_BALL_1_ASSET, GameAssets._SPIKE_BALL_FRAMES, GraphicID._ENEMY),
            new EntityDef("Spike Block", GraphicID.G_SPIKE_BLOCK_HORIZONTAL, MarkerID._SPIKE_BLOCK_TILE, GameAssets._SPIKE_BLOCK_ASSET, GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES, GraphicID._ENEMY),
            new EntityDef("Spike Block Vertical", GraphicID.G_SPIKE_BLOCK_VERTICAL, MarkerID._SPIKE_BLOCK_VERTICAL_TILE, GameAssets._SPIKE_BLOCK_VERTICAL_ASSET, GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES, GraphicID._ENEMY),
            new EntityDef("Loop Block", GraphicID.G_LOOP_BLOCK_HORIZONTAL, MarkerID._LOOP_BLOCK_HORIZONTAL_TILE, GameAssets._SPIKE_BLOCK_ASSET, GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES, GraphicID._ENEMY),
            new EntityDef("Loop Block Vertical", GraphicID.G_LOOP_BLOCK_VERTICAL, MarkerID._LOOP_BLOCK_VERTICAL_TILE, GameAssets._SPIKE_BLOCK_VERTICAL_ASSET, GameAssets._SPIKE_BLOCK_VERTICAL_FRAMES, GraphicID._ENEMY),

            // Miscellaneous Enemy Related
            new EntityDef("", GraphicID.G_ENEMY_BULLET, MarkerID._DEFAULT_TILE, GameAssets._PHASER_BULLET_ASSET, GameAssets._PHASER_BULLET_FRAMES, GraphicID._ENEMY),
//            G_EXPLOSION12,
//            G_EXPLOSION32,
//            G_EXPLOSION64,
//            G_EXPLOSION128,
//            G_EXPLOSION256,
        };

    public static int getEntityDefIndex(GraphicID _gid)
    {
        int index = 0;
        int defsIndex = 0;

        for (EntityDef def : entityList)
        {
            if (def.graphicID == _gid)
            {
                defsIndex = index;
            }

            index++;
        }

        return defsIndex;
    }

    public static EntityDef getEntityDef(GraphicID _gid)
    {
        return entityList[getEntityDefIndex(_gid)];
    }
}
