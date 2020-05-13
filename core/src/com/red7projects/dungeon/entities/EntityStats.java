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

import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;

//@formatter:off
public abstract class EntityStats
{
    public static int maxStormDemons;
    public static int maxBouncers;
    public static int maxScorpions;
    public static int maxSoldiers;

    public static int minStormDemons;
    public static int minBouncers;

    public static int numStormDemons;
    public static int numBouncers;
    public static int numScorpions;
    public static int numSoldiers;

    public static void initialise()
    {
        minStormDemons      = 0;
        minBouncers         = 0;
    }

    public static void clearMaxCounts()
    {
        maxStormDemons      = 0;
        maxBouncers         = 0;
        maxScorpions        = 0;
        maxSoldiers         = 0;
    }

    public static void log(GraphicID graphicID)
    {
        switch (graphicID)
        {
            case G_STORM_DEMON:
            {
                numStormDemons++;
            }
            break;

            case G_BOUNCER:
            {
                numBouncers++;
            }
            break;

            case G_SCORPION:
            {
                numScorpions++;
            }
            break;

            case G_SOLDIER:
            {
                numSoldiers++;
            }
            break;

            case G_COIN:
            case G_GEM:
            case G_KEY:
            case G_SHIELD:
            case G_TREASURE_CHEST:
            case G_SPIKE_BALL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_PRISONER:
            case G_VILLAGER:
            case G_POT:
            case G_CRATE:
            case G_BARREL:
            case G_ALCOVE_TORCH:
            case G_FLAME_THROWER:
            case G_DOOR:
            case G_LOCKED_DOOR:
            case G_FLOOR_BUTTON:
            case G_LEVER_SWITCH:
            case G_MYSTERY_CHEST:
            case G_FLOATING_PLATFORM:
            case G_TELEPORTER:
            case G_TURRET:
            case G_ESCALATOR:
            case G_ESCALATOR_LEFT:
            case G_ESCALATOR_RIGHT:
            case G_ESCALATOR_UP:
            case G_ESCALATOR_DOWN:
            case G_GLOW_EYES:
            case G_BOOK:
            case G_LITTER:
            case G_RUNE:
            case G_SACKS:
            {
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unable to log " + graphicID);
            }
            break;
        }
    }
}
