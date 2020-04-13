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
    public static int maxJellyMonsters;
    public static int maxScorpions;
    public static int maxBeetles;
    public static int maxSoldiers;

    public static int minStormDemons;
    public static int minBouncers;

    public static int numStormDemons;
    public static int numBouncers;
    public static int numJellyMonsters;
    public static int numScorpions;
    public static int numBeetles;
    public static int numSoldiers;

    private static int coinsCount;
    private static int gemsCount;
    private static int keysCount;
    private static int sheildsCount;
    private static int treasureChestsCount;
    private static int spikeBallCount;
    private static int spikeBlockCount;
    private static int loopBlockCount;
    private static int crateCount;
    private static int barrelCount;
    private static int potCount;
    private static int prisonerCount;
    private static int villagerCount;

    public static void initialise()
    {
        minStormDemons      = 0;
        minBouncers         = 0;
        coinsCount          = 0;
        gemsCount           = 0;
        keysCount           = 0;
        sheildsCount        = 0;
        treasureChestsCount = 0;
        spikeBallCount      = 0;
        spikeBlockCount     = 0;
        loopBlockCount      = 0;
        crateCount          = 0;
        barrelCount         = 0;
        potCount            = 0;
        prisonerCount       = 0;
        villagerCount       = 0;
    }

    public static void clearMaxCounts()
    {
        maxStormDemons      = 0;
        maxBouncers         = 0;
        maxJellyMonsters    = 0;
        maxScorpions        = 0;
        maxBeetles          = 0;
        maxSoldiers         = 0;
    }

    public static void log(GraphicID graphicID)
    {
        switch (graphicID)
        {
            case G_COIN:
            {
                coinsCount++;
            }
            break;

            case G_GEM:
            {
                gemsCount++;
            }
            break;

            case G_KEY:
            {
                keysCount++;
            }
            break;

            case G_SHIELD:
            {
                sheildsCount++;
            }
            break;

            case G_TREASURE_CHEST:
            {
                treasureChestsCount++;
            }
            break;

            case G_SPIKE_BALL:
            {
                spikeBallCount++;
            }
            break;

            case G_SPIKE_BLOCK_VERTICAL:
            case G_SPIKE_BLOCK_HORIZONTAL:
            {
                spikeBlockCount++;
            }
            break;

            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            {
                loopBlockCount++;
            }
            break;

            case G_PRISONER:
            {
                prisonerCount++;
            }
            break;

            case G_VILLAGER:
            {
                villagerCount++;
            }
            break;

            case G_STORM_DEMON:
            {
                numStormDemons++;
            }
            break;

            case G_JELLY_MONSTER:
            {
                numJellyMonsters++;
            }
            break;

            case G_BOUNCER:
            {
                numBouncers++;
            }
            break;

            case G_BEETLE:
            {
                numBeetles++;
            }
            break;

            case G_SCORPION:
            {
                numScorpions++;
            }
            break;

            case G_POT:
            {
                potCount++;
            }
            break;

            case G_CRATE:
            {
                crateCount++;
            }
            break;

            case G_BARREL:
            {
                barrelCount++;
            }
            break;

            case G_SOLDIER:
            {
                numSoldiers++;
            }
            break;

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

    static void report(App _app)
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("Entity Map Size    : ", _app.entityData.entityMap.size);
        Trace.divider();
        Trace.dbg("prisonerCount      : ", prisonerCount);
        Trace.dbg("villagerCount      : ", villagerCount);
        Trace.divider();
        Trace.dbg("coinsCount         : ", coinsCount);
        Trace.dbg("gemsCount          : ", gemsCount);
        Trace.dbg("keysCount          : ", keysCount);
        Trace.dbg("sheildsCount       : ", sheildsCount);
        Trace.dbg("treasureChestsCount: ", treasureChestsCount);
        Trace.divider();
        Trace.dbg("crateCount         : ", crateCount);
        Trace.dbg("barrelCount        : ", barrelCount);
        Trace.dbg("potCount           : ", potCount);
        Trace.divider();
        Trace.dbg("spikeBallCount     : ", spikeBallCount);
        Trace.dbg("spikeBlockCount    : ", spikeBlockCount);
        Trace.dbg("loopBlockCount     : ", loopBlockCount);
        Trace.divider();
        Trace.dbg("numBlueMines       : ", numStormDemons);
        Trace.dbg("numBouncers        : ", numBouncers);
        Trace.dbg("numJellyMonsters   : ", numJellyMonsters);
        Trace.dbg("numScorpions       : ", numScorpions);
        Trace.dbg("numBeetles         : ", numBeetles);
        Trace.dbg("numSoldiers        : ", numSoldiers);
        Trace.divider();
    }
}
